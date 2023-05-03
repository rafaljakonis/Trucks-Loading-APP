package com.example.trucksload.ui.login

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.trucksload.R
import com.example.trucksload.data.model.UserToAuthenticate
import com.example.trucksload.databinding.FragmentLoginScreenBinding
import com.example.trucksload.util.Constants.APPLICATION_NAME
import com.example.trucksload.util.Constants.APPLICATION_VERSION
import com.example.trucksload.util.NetworkResult
import com.example.trucksload.viewmodels.LoginViewModel
import com.example.trucksload.viewmodels.MainViewModel
import com.example.trucksload.viewmodels.SharedViewModel
import com.google.android.material.snackbar.Snackbar
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

        watchFormState()

        binding.loginButton.setOnClickListener {
            val login: String = binding.loginInputData.text.toString()
            val password: String = binding.passwordInputData.text.toString()

            if (login.isNotEmpty() && password.isNotEmpty()) {
                val userToAuthenticate = UserToAuthenticate(
                    login,
                    password,
                    APPLICATION_NAME,
                    APPLICATION_VERSION,
                    "TEST"
                )
                findNavController().navigate(R.id.action_loginScreenFragment_to_taskListFragment)
                mainViewModel.authenticateUser(userToAuthenticate)
                handleLoginResponse()
            }
        }

        return binding.root
    }

    private fun watchFormState(): Unit {
        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer

            binding.loginButton.isClickable = loginState.isDataValid
            binding.loginButton.isEnabled = loginState.isDataValid

            if (loginState.isDataValid) {
                binding.loginButton.setBackgroundColor(getColor(requireContext(), R.color.blue_700))
            } else {
                binding.loginButton.setBackgroundColor(getColor(requireContext(), R.color.gray))
            }
        })

        binding.loginInputData.afterTextChanged {
            loginViewModel.loginDataChanged(
                binding.loginInputData.text.toString(),
                binding.passwordInputData.text.toString()
            )
        }

        binding.passwordInputData.afterTextChanged {
            loginViewModel.loginDataChanged(
                binding.loginInputData.text.toString(),
                binding.passwordInputData.text.toString()
            )
        }
    }

    private fun handleLoginResponse(): Unit {
        mainViewModel.authenticateUserResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    dropProgressBar()
                    findNavController().navigate(R.id.action_loginScreenFragment_to_taskListFragment)
                }

                is NetworkResult.Error -> {
                    dropProgressBar()
                    clearLoginInputs()
                    showSnackBar(response.message.toString())
                }

                is NetworkResult.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun showSnackBar(message: String): Unit {
        Snackbar.make(binding.loginConstraintLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun dropProgressBar(): Unit {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar(): Unit {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun clearLoginInputs(): Unit {
        binding.loginInputData.text?.clear()
        binding.passwordInputData.text?.clear()
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