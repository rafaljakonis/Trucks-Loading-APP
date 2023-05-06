package com.example.trucksload.ui.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.trucksload.R
import com.example.trucksload.data.model.Task
import com.example.trucksload.data.network.model.AssignOrderToUser
import com.example.trucksload.databinding.FragmentBottomSheetBinding
import com.example.trucksload.util.NetworkResult
import com.example.trucksload.viewmodels.MainViewModel
import com.example.trucksload.viewmodels.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheet(
    private val pickedTask: Task
) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        setUpBottomSheetDetails()
        setUpObservers()

        binding.bottomSheetConfirmButton.setOnClickListener {
            mainViewModel.assignOrderToUser(
                AssignOrderToUser(
                    sharedViewModel.user.id,
                    pickedTask.id
                )
            )
        }

        binding.bottomSheetCancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    private fun setUpObservers() {
        mainViewModel.assignOrderToUserResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    dropProgressBar()
                    if (response.data?.result == true) {
                        dismiss()
                        findNavController().navigate(
                            R.id.action_taskListFragment_to_taskDetailsFragment,
                            bundleOf("task" to pickedTask.id)
                        )
                    } else {
                        showSnackBar(getString(R.string.order_are_operated))
                        dismiss()
                    }
                }

                is NetworkResult.Error -> {
                    dropProgressBar()
                    showSnackBar(getString(R.string.something_is_wrong))
                    dismiss()
                }

                is NetworkResult.Loading -> {
                    showProgressBar()
                }
            }

        }
    }

    private fun showProgressBar() {
        binding.bottomSheetProgressBar.visibility = View.VISIBLE
    }

    private fun dropProgressBar() {
        binding.bottomSheetProgressBar.visibility = View.GONE
    }

    private fun showSnackBar(message: String) {
        parentFragment?.view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG).show() }
    }

    private fun setUpBottomSheetDetails() {
        binding.bottomSheetTaskName.append(" ${pickedTask.id}")
        binding.bottomSheetDescription.text = pickedTask.description
        binding.bottomSheetLocation.append(" ${pickedTask.location}")
        binding.bottomSheetStatus.append(" ${pickedTask.statusName}")
    }

}