package com.example.trucksload.ui.login

import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.trucksload.R
import com.example.trucksload.databinding.FragmentLoginScreenBinding
import com.example.trucksload.viewmodels.MainViewModel
import com.example.trucksload.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LoginScreenFragment : Fragment() {

    private var _binding: FragmentLoginScreenBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginScreenBinding.inflate(layoutInflater, container, false)

//        binding.mainViewModel = mainViewModel
        try {
            mainViewModel.getActiveOrders()
            mainViewModel.activeOrders.observe(viewLifecycleOwner) { response ->
                Log.i("Test", response.data.toString())
            }
        } catch (e: Exception) {
            Log.i("Test", e.toString())
        }


        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreenFragment_to_taskListFragment)
        }

        return binding.root
    }

    private fun dropProgressBar(): Unit
    {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar(): Unit
    {
        binding.progressBar.visibility = View.VISIBLE
    }
}