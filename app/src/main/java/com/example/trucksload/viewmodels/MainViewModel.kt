package com.example.trucksload.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.*
import com.example.trucksload.data.Repository
import com.example.trucksload.data.database.TruckLoadEntity
import com.example.trucksload.data.model.Task
import com.example.trucksload.data.model.User
import com.example.trucksload.data.model.UserToAuthenticate
import com.example.trucksload.data.network.model.BooleanResult
import com.example.trucksload.data.network.model.AssignOrderToUser
import com.example.trucksload.data.network.model.OrderActionRequest
import com.example.trucksload.data.network.model.ScannedElement
import com.example.trucksload.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    val user: LiveData<TruckLoadEntity> = repository.local.readUserProperty().asLiveData()

    fun insertUser(userLoadEntity: TruckLoadEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertUser(userLoadEntity)
        }

    fun deleteUser() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteUser()
        }

    //API
    var authenticateUserResponse: MutableLiveData<NetworkResult<User>> = MutableLiveData()
    var activeOrdersResponse: MutableLiveData<NetworkResult<ArrayList<Task>>> = MutableLiveData()
    var assignOrderToUserResponse: MutableLiveData<NetworkResult<BooleanResult>> = MutableLiveData()
    var cancelOrderResponse: MutableLiveData<NetworkResult<BooleanResult>> = MutableLiveData()
    var finishOrderResponse: MutableLiveData<NetworkResult<BooleanResult>> = MutableLiveData()
    var scannedElementResponse: MutableLiveData<NetworkResult<BooleanResult>> = MutableLiveData()
    var uploadPhotoResponse: MutableLiveData<NetworkResult<BooleanResult>> = MutableLiveData()
    var operatedOrderResponse: MutableLiveData<NetworkResult<ArrayList<Task>>> = MutableLiveData()

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

    fun finishOrder(finishOrder: OrderActionRequest) = viewModelScope.launch {
        finishOrderSafe(finishOrder)
    }

    fun setElementScanned(scannedElement: ScannedElement) = viewModelScope.launch {
        setElementScannedSafe(scannedElement)
    }

    fun uploadPhoto(orderId: RequestBody, file: MultipartBody.Part) = viewModelScope.launch {
        uploadPhotoSafe(orderId, file)
    }
    fun getOperatedOrder(userId: Int) = viewModelScope.launch {
        getOperatedOrderSafe(userId)
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
                activeOrdersResponse.value =
                    handleResponse(response) as NetworkResult<ArrayList<Task>>
            } catch (e: Exception) {
                activeOrdersResponse.value = NetworkResult.Error("Wystąpił problem")
            }
        } else {
            activeOrdersResponse.value = NetworkResult.Error("Brak połączenia z internetem")
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
                return NetworkResult.Error("Wystąpił problem")
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
                assignOrderToUserResponse.value =
                    handleResponse(response) as NetworkResult<BooleanResult>
            } catch (e: Exception) {
                Log.i("API", e.message.toString())
                assignOrderToUserResponse.value = NetworkResult.Error("Wystąpił problem")
            }
        } else {
            assignOrderToUserResponse.value = NetworkResult.Error("Brak połączenia z internetem")
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
                cancelOrderResponse.value = NetworkResult.Error("Wystąpił problem")
            }
        } else {
            cancelOrderResponse.value = NetworkResult.Error("Brak połączenia z internetem")
        }
    }

    private suspend fun finishOrderSafe(finishOrder: OrderActionRequest) {
        finishOrderResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                delay(1000)
                val response = repository.remote.finishOrder(finishOrder)
                finishOrderResponse.value = handleResponse(response) as NetworkResult<BooleanResult>
            } catch (e: Exception) {
                Log.i("API", e.message.toString())
                finishOrderResponse.value = NetworkResult.Error("Wystąpił problem")
            }
        } else {
            finishOrderResponse.value = NetworkResult.Error("Brak połączenia z internetem")
        }
    }

    private suspend fun setElementScannedSafe(scannedElement: ScannedElement) {
        scannedElementResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                delay(1000)
                val response = repository.remote.setElementScanned(scannedElement)
                scannedElementResponse.value =
                    handleResponse(response) as NetworkResult<BooleanResult>
            } catch (e: Exception) {
                Log.i("API", e.message.toString())
                scannedElementResponse.value = NetworkResult.Error("Wystąpił problem")
            }
        } else {
            scannedElementResponse.value = NetworkResult.Error("Brak połączenia z internetem")
        }
    }

    private suspend fun uploadPhotoSafe(orderId: RequestBody, file: MultipartBody.Part) {
        uploadPhotoResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                delay(1000)
                val response = repository.remote.uploadPhoto(orderId, file)
                uploadPhotoResponse.value = handleResponse(response) as NetworkResult<BooleanResult>
            } catch (e: Exception) {
                Log.i("API", e.message.toString())
                uploadPhotoResponse.value = NetworkResult.Error("Wystąpił problem")
            }
        } else {
            uploadPhotoResponse.value = NetworkResult.Error("Brak połączenia z internetem")
        }
    }

    private suspend fun getOperatedOrderSafe(userId: Int) {
        operatedOrderResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                delay(1000)
                val response = repository.remote.getOperatedOrder(userId)
                operatedOrderResponse.value = handleResponse(response) as NetworkResult<ArrayList<Task>>
            } catch (e: Exception) {
                operatedOrderResponse.value = NetworkResult.Error("Wystąpił problem")
            }
        } else {
            operatedOrderResponse.value = NetworkResult.Error("Brak połączenia z internetem")
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