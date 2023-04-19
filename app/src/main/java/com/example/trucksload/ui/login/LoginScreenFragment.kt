package com.example.trucksload.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.trucksload.R
import com.example.trucksload.databinding.FragmentLoginScreenBinding
import com.example.trucksload.util.NetworkResult
import com.example.trucksload.viewmodels.LoginViewModel
import com.example.trucksload.viewmodels.MainViewModel
import com.example.trucksload.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginScreenFragment : Fragment() {

    private var _binding: FragmentLoginScreenBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginScreenBinding.inflate(layoutInflater, container, false)

        binding.loginButton.setOnClickListener {
            val login: String = binding.loginInputData.text.toString()
            val password: String = binding.passwordInputData.text.toString()

            if (login.isNotEmpty() && password.isNotEmpty()) {
                mainViewModel.getActiveOrders()
                handleLoginResponse()
            }

        }

        return binding.root
    }


    private fun watchFormState(): Unit {
        loginViewModel.loginFormState.observe(viewLifecycleOwner) { formState ->
            when {
                formState.isDataValid -> {

                }
                formState.usernameError != null -> {
                    binding.loginInput.error = getString(formState.usernameError)
                }
                formState.passwordError != null -> {
                    binding.passwordInput.error = getString(formState.passwordError)
                }
            }
//            login.isEnabled = loginState.isDataValid
//            if (loginState.isDataValid) {
//                login.backgroundTintList =
//                    ColorStateList.valueOf(getColor(R.color.dark_primary))
//            }
        }

        binding.loginInputData.afterTextChanged {
            loginViewModel.loginDataChanged(
                binding.loginInputData.text.toString(),
                binding.passwordInputData.text.toString()
            )
        }

        binding.passwordInputData.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    binding.loginInputData.text.toString(),
                    binding.passwordInputData.text.toString()
                )
            }

        }
    }


    private fun handleLoginResponse(): Unit {
        mainViewModel.activeOrders.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    dropProgressBar()
                    findNavController().navigate(R.id.action_loginScreenFragment_to_taskListFragment)
                }

                is NetworkResult.Error -> {
                    dropProgressBar()
                }

                is NetworkResult.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun dropProgressBar(): Unit {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar(): Unit {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

}