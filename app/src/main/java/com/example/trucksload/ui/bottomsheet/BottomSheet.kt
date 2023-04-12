package com.example.trucksload.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.trucksload.R
import com.example.trucksload.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet() : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)

        binding.confirmButton.setOnClickListener {
            findNavController().navigate(R.id.action_taskListFragment_to_taskDetailsFragment)
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}