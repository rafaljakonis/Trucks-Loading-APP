package com.example.trucksload.ui.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trucksload.R
import com.example.trucksload.adapter.TaskAdapter
import com.example.trucksload.databinding.FragmentTaskDetailsBinding
import com.example.trucksload.databinding.FragmentTaskListBinding
import com.example.trucksload.ui.bottomsheet.BottomSheet
import com.example.trucksload.viewmodels.SharedViewModel

class TaskDetailsFragment : Fragment() {

    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailsBinding.inflate(layoutInflater, container, false)

        binding.confirmButton.setOnClickListener {
            findNavController().navigate(R.id.taskListFragment)
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigate(R.id.taskListFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}