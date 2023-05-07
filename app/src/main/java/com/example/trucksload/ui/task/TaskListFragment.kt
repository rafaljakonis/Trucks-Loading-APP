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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trucksload.R
import com.example.trucksload.adapter.TaskAdapter
import com.example.trucksload.data.model.Element
import com.example.trucksload.data.model.Task
import com.example.trucksload.databinding.FragmentLoginScreenBinding
import com.example.trucksload.databinding.FragmentTaskListBinding
import com.example.trucksload.ui.bottomsheet.BottomSheet
import com.example.trucksload.util.NetworkResult
import com.example.trucksload.viewmodels.MainViewModel
import com.example.trucksload.viewmodels.SharedViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var adapterArray: ArrayList<Task> = arrayListOf()
    private val adapter: TaskAdapter by lazy { TaskAdapter(adapterArray) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListBinding.inflate(layoutInflater, container, false)
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.tasksRecyclerView.adapter = adapter

        setOrdersObserver()
        mainViewModel.getActiveOrders()

        binding.topBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sign_out -> logout()
                R.id.refresh -> refresh()
            }
            true
        }

        adapter.onItemClick = {
            BottomSheet(it).show(parentFragmentManager, "ModalBottomSheet")
        }

        return binding.root
    }

    private fun setOrdersObserver() {
        mainViewModel.activeOrdersResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    stopShimmerEffect()
                    dropLackOfInternetInformation()
                    sharedViewModel.orderArrayList = response.data!!

                    if (response.data.isEmpty()) {
                        showEmptyDataInformation()
                    } else {
                        dropEmptyDataInformation()
                    }

                    adapterArray.plusAssign(response.data)
                    adapter.notifyDataSetChanged()
                }

                is NetworkResult.Error -> {
                    showLackOfInternetInformation()
                    stopShimmerEffect()
                }

                is NetworkResult.Loading -> {
                    startShimmerEffect()
                }
            }
        }
    }

    private fun showEmptyDataInformation() {
        dropLackOfInternetInformation()
        binding.lackOfDataImage.visibility = View.VISIBLE
        binding.lackOfDataText.visibility = View.VISIBLE
    }

    private fun dropEmptyDataInformation() {
        binding.lackOfDataImage.visibility = View.GONE
        binding.lackOfDataText.visibility = View.GONE
    }

    private fun showLackOfInternetInformation() {
        dropEmptyDataInformation()
        binding.lackOfInternet.visibility = View.VISIBLE
        binding.lackOfInternetText.visibility = View.VISIBLE
    }

    private fun dropLackOfInternetInformation() {
        binding.lackOfInternet.visibility = View.GONE
        binding.lackOfInternetText.visibility = View.GONE
    }

    private fun startShimmerEffect() {
        dropEmptyDataInformation()
        dropLackOfInternetInformation()
        binding.shimmerEffectLayout.startShimmer()
        binding.tasksRecyclerView.visibility = View.GONE
        binding.shimmerEffectLayout.visibility = View.VISIBLE
    }

    private fun stopShimmerEffect() {
        binding.shimmerEffectLayout.stopShimmer()
        binding.tasksRecyclerView.visibility = View.VISIBLE
        binding.shimmerEffectLayout.visibility = View.GONE
    }

    private fun logout() {
        mainViewModel.deleteUser()
        findNavController().navigate(R.id.loginScreenFragment)
    }

    private fun refresh() {
        binding.tasksRecyclerView.removeAllViews()
        adapterArray.clear()
        adapter.notifyDataSetChanged()
        mainViewModel.getActiveOrders()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}