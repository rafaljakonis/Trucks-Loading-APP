package com.example.trucksload.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.trucksload.data.PutAwayResponse
import com.example.trucksload.data.Repository
import com.example.trucksload.data.model.User
import com.example.trucksload.data.model.UserToAuthenticate
import com.example.trucksload.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    //ROOM
    var recyclerViewState: Parcelable? = null


    //API
    var authenticateUserResponse: MutableLiveData<NetworkResult<User>> = MutableLiveData()
    var activeOrders: MutableLiveData<NetworkResult<PutAwayResponse>> = MutableLiveData()

    fun getActiveOrders() = viewModelScope.launch {
        getActiveOrdersSafeCall()
    }

    fun authenticateUser(userToAuthenticate: UserToAuthenticate) = viewModelScope.launch {
        authenticateUserSafe(userToAuthenticate)
    }

    private suspend fun authenticateUserSafe(userToAuthenticate: UserToAuthenticate) {
        authenticateUserResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                delay(500)
                val response = repository.remote.authenticateUser(userToAuthenticate)
                authenticateUserResponse.value = handleAuthenticateUserResponse(response)
            } catch (e: Exception) {
                authenticateUserResponse.value = NetworkResult.Error("Wystąpił problem z serwerem")
            }
        } else {
            authenticateUserResponse.value = NetworkResult.Error("Brak połączenia z internetem")
        }
    }

    private fun handleAuthenticateUserResponse(response: Response<User>): NetworkResult<User> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Za długi czas na odpowiedź")
            }
            response.code() == 400 -> {
                return NetworkResult.Error("Brak wymaganych danych")
            }
            response.code() == 401 -> {
                return NetworkResult.Error("Błędne dane logowania")
            }
            response.body().toString().isEmpty() -> {
                return NetworkResult.Error("Wystąpił problem z serwerem")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }


    private suspend fun getActiveOrdersSafeCall() {
        activeOrders.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getOrders()
                activeOrders.value = handleResponse(response) as NetworkResult<PutAwayResponse>

                val activeOrders = activeOrders.value!!.data
            } catch (e: Exception) {
                activeOrders.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            activeOrders.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleResponse(response: Response<*>): NetworkResult<Any> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body().toString().isEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}