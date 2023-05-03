package com.example.trucksload.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.trucksload.R
import com.example.trucksload.data.model.Task
import com.example.trucksload.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet(
    private val pickedTask: Task
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        setUpBottomSheetDetails()

        binding.confirmButton.setOnClickListener {
            findNavController().navigate(R.id.action_taskListFragment_to_taskDetailsFragment)
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
    private fun setUpBottomSheetDetails() {
        binding.taskName.text = "Zadanie ${pickedTask.id}"
        binding.taskDescription.text = pickedTask.description
        binding.taskLocation.text = pickedTask.location
        binding.taskStatus.text = pickedTask.status.toString()
    }
}