package com.example.trucksload.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat.getColor
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.trucksload.R
import com.example.trucksload.data.database.TruckLoadEntity
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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginScreenFragment : Fragment() {

    private lateinit var binding: FragmentLoginScreenBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginScreenBinding.inflate(layoutInflater, container, false)
        readSavedUser()
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
                mainViewModel.authenticateUser(userToAuthenticate)
                handleLoginResponse()
            }
        }

        binding

        return binding.root
    }

    private fun readSavedUser() {
        lifecycleScope.launch {
            mainViewModel.user.observe(viewLifecycleOwner, Observer { result ->
                if (result != null) {
                    binding.loginInputData.setText(result.uid)
                    binding.passwordInputData.setText(result.password)
                    binding.rememberMe.isChecked = true
                }
            })
        }
    }

    private fun watchFormState() {
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

    private fun handleLoginResponse() {
        mainViewModel.authenticateUserResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        sharedViewModel.user = it

                        if (binding.rememberMe.isChecked) {
                            mainViewModel.insertUser(
                                TruckLoadEntity(
                                    it.uid,
                                    binding.passwordInputData.text.toString(),
                                    it.firstName,
                                    it.lastName
                                )
                            )
                        }

                        mainViewModel.getOperatedOrder(it.id)
                    }
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
        mainViewModel.operatedOrderResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    dropProgressBar()
                    if (response.data!!.isEmpty()) {
                        findNavController().navigate(R.id.action_loginScreenFragment_to_taskListFragment)
                    } else {
                        sharedViewModel.orderArrayList = response.data
                        findNavController().navigate(
                            R.id.action_loginScreenFragment_to_taskDetailsFragment,
                            bundleOf("task" to response.data.first().id)
                        )
                    }
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

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.loginConstraintLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun dropProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun clearLoginInputs() {
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