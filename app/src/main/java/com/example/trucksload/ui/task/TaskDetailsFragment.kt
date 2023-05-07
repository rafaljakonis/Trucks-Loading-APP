package com.example.trucksload.ui.task

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.trucksload.R
import com.example.trucksload.data.model.Task
import com.example.trucksload.data.network.model.OrderActionRequest
import com.example.trucksload.data.network.model.ScannedElement
import com.example.trucksload.databinding.FragmentTaskDetailsBinding
import com.example.trucksload.util.NetworkResult
import com.example.trucksload.viewmodels.MainViewModel
import com.example.trucksload.viewmodels.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class TaskDetailsFragment : Fragment() {
    private lateinit var photoFile: File
    private lateinit var currentPhotoPath: String
    private lateinit var chooseTask: Task
    private lateinit var binding: FragmentTaskDetailsBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskDetailsBinding.inflate(layoutInflater, container, false)

        val orderId = arguments?.getInt("task")
        chooseTask = orderId?.let { sharedViewModel.getOrderById(it) }!!
        setUpOrderProperty()
        setUpObservers()

        binding.qrButton.setOnClickListener {
            scanQrCodeLauncher.launch(null)
        }

        binding.photoButton.setOnClickListener {
            startCamera()
        }

        binding.confirmButton.setOnClickListener {
            mainViewModel.finishOrder(OrderActionRequest(chooseTask.id))
        }

        binding.cancelButton.setOnClickListener {
            mainViewModel.cancelOrder(OrderActionRequest(chooseTask.id))
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setUpOrderProperty() {
        binding.detailOrderName.text = "Zamówienie ${chooseTask.id}"
        binding.detailOrderDescription.text = chooseTask.description
        binding.detailOrderLocation.text = "Lokalizacja: ${chooseTask.location}"
        binding.detailOrderStatus.text = "Status: ${chooseTask.statusName}"
        setUpElementsProperty()
    }

    private fun setUpElementsProperty() {
        binding.elementsLinearLayout.removeAllViews()

        chooseTask.elements.forEach {
            val temporaryTextView: TextView = TextView(context)
            var temporaryDrawable: Int = R.drawable.ic_check_green

            if (it.isComplete == 0) {
                temporaryDrawable = R.drawable.ic_close_red
            }

            temporaryTextView.width = ViewGroup.LayoutParams.WRAP_CONTENT
            temporaryTextView.text = "- ${it.name}"
            temporaryTextView.setPadding(40, 0, 40, 0)
            temporaryTextView.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                temporaryDrawable,
                0
            )
            binding.elementsLinearLayout.addView(temporaryTextView)
        }
    }

    private fun setUpObservers() {
        mainViewModel.cancelOrderResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    dropProgressBar()
                    findNavController().navigate(R.id.taskListFragment)
                }

                is NetworkResult.Error -> {
                    showSnackBar(getString(R.string.something_is_wrong))
                    dropProgressBar()
                }

                is NetworkResult.Loading -> {
                    showProgressBar()
                }
            }
        }

        mainViewModel.finishOrderResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    dropProgressBar()
                    findNavController().navigate(R.id.taskListFragment)
                }

                is NetworkResult.Error -> {
                    showSnackBar(getString(R.string.something_is_wrong))
                    dropProgressBar()
                }

                is NetworkResult.Loading -> {
                    showProgressBar()
                }
            }
        }

        mainViewModel.scannedElementResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    dropProgressBar()
                    setUpElementsProperty()
                    showSnackBar(getString(R.string.success_save))
                }

                is NetworkResult.Error -> {
                    showSnackBar(getString(R.string.something_is_wrong))
                    dropProgressBar()
                }

                is NetworkResult.Loading -> {
                    showProgressBar()
                }
            }
        }

        mainViewModel.uploadPhotoResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    dropProgressBar()
                    showSnackBar(getString(R.string.success_save))
                }

                is NetworkResult.Error -> {
                    showSnackBar(getString(R.string.something_is_wrong))
                    dropProgressBar()
                }

                is NetworkResult.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
        when (result) {
            is QRResult.QRSuccess -> checkAndSafeScannedCode(result.content.rawValue).toString()
            is QRResult.QRError -> showSnackBar(getString(R.string.something_is_wrong))
            else -> showSnackBar(getString(R.string.scanned_stopped))
        }
    }

    private fun checkAndSafeScannedCode(scannedCode: String) {
        val newScannedCode = scannedCode.replace("\n", "")
        val elementId = checkScannedCode(newScannedCode)

        if (elementId != -1) {
            mainViewModel.setElementScanned(ScannedElement(chooseTask.id, elementId, scannedCode))
        } else {
            showSnackBar(getString(R.string.wrong_qr_code))
        }
    }

    private fun checkScannedCode(scannedCode: String): Int {
        chooseTask.elements.forEach {
            if (it.code == scannedCode && it.isComplete == 0) {
                it.isComplete = 1

                return it.id
            }
        }

        return -1
    }

    private fun startCamera() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = createImageFile()
        val uri = context?.let {
            FileProvider.getUriForFile(
                it,
                "com.example.trucksload.fileprovider",
                photoFile
            )
        }
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        captureImage.launch(pictureIntent)
    }

    private val captureImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val requestFile = photoFile.asRequestBody()
                val image = MultipartBody.Part.createFormData("image", photoFile.name, requestFile)
                val oderIdRequestBody = chooseTask.id.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                mainViewModel.uploadPhoto(oderIdRequestBody, image)
            }
        }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun disableActionButtons() {
        binding.cancelButton.isCheckable = false
        binding.confirmButton.isCheckable = false
        binding.qrButton.isCheckable = false
        binding.photoButton.isCheckable = false

        binding.cancelButton.setBackgroundColor(Color.GRAY)
        binding.confirmButton.setBackgroundColor(Color.GRAY)
        binding.qrButton.setBackgroundColor(Color.GRAY)
        binding.photoButton.setBackgroundColor(Color.GRAY)
    }

    private fun enableActionButtons() {
        binding.cancelButton.isCheckable = true
        binding.confirmButton.isCheckable = true
        binding.qrButton.isCheckable = true
        binding.photoButton.isCheckable = true

        binding.cancelButton.setBackgroundColor(resources.getColor(R.color.red))
        binding.confirmButton.setBackgroundColor(resources.getColor(R.color.green))
        binding.qrButton.setBackgroundColor(resources.getColor(R.color.blue_700))
        binding.photoButton.setBackgroundColor(resources.getColor(R.color.pink_900))
    }

    private fun showSnackBar(message: String): Unit {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showProgressBar() {
        disableActionButtons()
        binding.detailProgressBar.visibility = View.VISIBLE
    }

    private fun dropProgressBar() {
        enableActionButtons()
        binding.detailProgressBar.visibility = View.GONE
    }
}