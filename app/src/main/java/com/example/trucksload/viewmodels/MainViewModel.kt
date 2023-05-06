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
import com.example.trucksload.data.Repository
import com.example.trucksload.data.model.Task
import com.example.trucksload.data.model.User
import com.example.trucksload.data.model.UserToAuthenticate
import com.example.trucksload.data.network.model.BooleanResult
import com.example.trucksload.data.network.model.AssignOrderToUser
import com.example.trucksload.data.network.model.OrderActionRequest
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
    var activeOrdersResponse: MutableLiveData<NetworkResult<ArrayList<Task>>> = MutableLiveData()
    var assignOrderToUserResponse: MutableLiveData<NetworkResult<BooleanResult>> = MutableLiveData()
    var cancelOrderResponse: MutableLiveData<NetworkResult<BooleanResult>> = MutableLiveData()

    fun getActiveOrders() = viewModelScope.launch {
        getActiveOrdersSafeCall()
    }

    fun assignOrderToUser(assignOrderToUser: AssignOrderToUser) = viewModelScope.launch {
        assignOrderToUserSafe(assignOrderToUser)
    }

    fun authenticateUser(userToAuthenticate: UserToAuthenticate) = viewModelScope.launch {
        authenticateUserSafe(userToAuthenticate)
    }

    fun cancelOrder(cancelOrder: OrderActionRequest) = viewModelScope.launch {
        cancelOrderSafe(cancelOrder)
    }

    private suspend fun authenticateUserSafe(userToAuthenticate: UserToAuthenticate) {
        authenticateUserResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                delay(1000)
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
        activeOrdersResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                delay(1000)
                val response = repository.remote.getOrders()
                activeOrdersResponse.value = handleResponse(response) as NetworkResult<ArrayList<Task>>
            } catch (e: Exception) {
                Log.i("API", e.message.toString())
                activeOrdersResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            activeOrdersResponse.value = NetworkResult.Error("No Internet Connection.")
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

    private suspend fun assignOrderToUserSafe(assignOrderToUser: AssignOrderToUser) {
        assignOrderToUserResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                delay(1000)
                val response = repository.remote.assignOrderToUser(assignOrderToUser)
                assignOrderToUserResponse.value = handleResponse(response) as NetworkResult<BooleanResult>
            } catch (e: Exception) {
                Log.i("API", e.message.toString())
                assignOrderToUserResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            assignOrderToUserResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }
    private suspend fun cancelOrderSafe(cancelOrder: OrderActionRequest) {
        cancelOrderResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                delay(1000)
                val response = repository.remote.cancelOrder(cancelOrder)
                cancelOrderResponse.value = handleResponse(response) as NetworkResult<BooleanResult>
            } catch (e: Exception) {
                Log.i("API", e.message.toString())
                cancelOrderResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            cancelOrderResponse.value = NetworkResult.Error("No Internet Connection.")
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