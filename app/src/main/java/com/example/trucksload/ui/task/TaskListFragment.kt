package com.example.trucksload.ui.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trucksload.R
import com.example.trucksload.adapter.TaskAdapter
import com.example.trucksload.databinding.FragmentLoginScreenBinding
import com.example.trucksload.databinding.FragmentTaskListBinding
import com.example.trucksload.ui.bottomsheet.BottomSheet
import com.example.trucksload.viewmodels.SharedViewModel

class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val adapter: TaskAdapter by lazy { TaskAdapter(sharedViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(layoutInflater, container, false)
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.tasksRecyclerView.adapter = adapter

        adapter.onItemClick = {
            BottomSheet().show(parentFragmentManager, "ModalBottomSheet")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}