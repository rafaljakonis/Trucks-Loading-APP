package com.example.trucksload.ui.task

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.trucksload.R
import com.example.trucksload.data.model.Task
import com.example.trucksload.data.network.model.OrderActionRequest
import com.example.trucksload.databinding.FragmentTaskDetailsBinding
import com.example.trucksload.util.NetworkResult
import com.example.trucksload.viewmodels.MainViewModel
import com.example.trucksload.viewmodels.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TaskDetailsFragment : Fragment() {
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
            val test =  IntentIntegrator(activity)
            test.setOrientationLocked(false)
            test.setPrompt("tsdfsdsd")
            test.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            test.initiateScan()
            
        }

        binding.confirmButton.setOnClickListener {
            findNavController().navigate(R.id.taskListFragment)
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode, resultCode, data)
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
    }

    private fun showSnackBar(message: String): Unit {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
    private fun showProgressBar() {
        binding.detailProgressBar.visibility = View.VISIBLE
    }

    private fun dropProgressBar() {
        binding.detailProgressBar.visibility = View.VISIBLE
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }


}