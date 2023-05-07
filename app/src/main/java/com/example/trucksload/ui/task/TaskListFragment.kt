package com.example.trucksload.ui.task

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trucksload.R
import com.example.trucksload.adapter.TaskAdapter
import com.example.trucksload.data.model.Element
import com.example.trucksload.data.model.Task
import com.example.trucksload.databinding.FragmentLoginScreenBinding
import com.example.trucksload.databinding.FragmentTaskListBinding
import com.example.trucksload.ui.bottomsheet.BottomSheet
import com.example.trucksload.viewmodels.SharedViewModel
import com.google.android.material.chip.Chip

class TaskListFragment : Fragment() {

    private var adapterArray: ArrayList<Task> = arrayListOf()
    private var adapterArray1: ArrayList<Task> = arrayListOf()
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val adapter: TaskAdapter by lazy { TaskAdapter(adapterArray) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(layoutInflater, container, false)
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.tasksRecyclerView.adapter = adapter
        test()
        t2()
        binding.statusChoices.forEach { child ->
            (child as? Chip)?.setOnCheckedChangeListener { _, _ ->
                Log.i("TEST", child.text.toString())
                when (child.text.toString()) {
                    "Podstawienie" -> sharedViewModel.chipSubstitute = child.isChecked
                    "Załadunek" -> sharedViewModel.chipLoading = child.isChecked
                    "Odstawienie" -> sharedViewModel.chipPutAway = child.isChecked
                }
                t1()
            }
        }
        adapter.onItemClick = {
            BottomSheet(it).show(parentFragmentManager, "ModalBottomSheet")
        }

        return binding.root
    }

    private fun t2() {
        binding.chipSubstitute.isChecked = sharedViewModel.chipSubstitute
        binding.chipLoading.isChecked = sharedViewModel.chipLoading
        binding.chipPutAway.isChecked = sharedViewModel.chipPutAway
        t1();
    }
    private fun t1() {
        adapterArray.clear()
        adapterArray1.forEach {
            if (it.status == 1 && sharedViewModel.chipSubstitute) adapterArray.plusAssign(it)
            if (it.status == 2 && sharedViewModel.chipLoading) adapterArray.plusAssign(it)
            if (it.status == 3 && sharedViewModel.chipPutAway) adapterArray.plusAssign(it)
        }
        adapter.notifyDataSetChanged()
    }

    private fun test() {
        adapterArray1.plusAssign(
            Task(
                1,
                123,
                1,
                "1231",
                "sdfsdf",
                "2024-01-12",
                false,
                listOf<Element>(
                    Element("123", "123", 123, "TEST", false),
                    Element("123", "123", 123, "TEST", false),
                    Element("123", "123", 123, "TEST", false),
                    Element("123", "123", 123, "TEST", false)
                )
            )
        )
        adapterArray1.plusAssign(
            Task(
                1,
                123,
                1,
                "1231",
                "sdfsdf",
                "2024-01-12",
                false,
                listOf<Element>(
                    Element("123", "123", 123, "TEST", false),
                    Element("123", "123", 123, "TEST", false),
                    Element("123", "123", 123, "TEST", false),
                    Element("123", "123", 123, "TEST", false)
                )
            )
        )
        adapterArray1.plusAssign(
            Task(
                1,
                123,
                3,
                "1231",
                "sdfsdf",
                "2024-01-12",
                false,
                listOf<Element>(
                    Element("123", "123", 123, "TEST", false),
                    Element("123", "123", 123, "TEST", false),
                    Element("123", "123", 123, "TEST", false),
                    Element("123", "123", 123, "TEST", false)
                )
            )
        )
        adapterArray1.plusAssign(
            Task(
                1,
                123,
                2,
                "1231",
                "sdfsdf",
                "2024-01-12",
                false,
                listOf<Element>(
                    Element("123", "123", 123, "TEST", false),
                    Element("123", "123", 123, "TEST", false),
                    Element("123", "123", 123, "TEST", false),
                    Element("123", "123", 123, "TEST", false)
                )
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}