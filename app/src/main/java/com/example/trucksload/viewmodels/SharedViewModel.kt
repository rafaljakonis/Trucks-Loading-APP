package com.example.trucksload.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.trucksload.data.DataStoreRepository
import com.example.trucksload.data.model.Task
import com.example.trucksload.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    val app = application
    var orderArrayList: ArrayList<Task> =arrayListOf()
    lateinit var user: User

    fun saveFirstLaunch(firstLaunch: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveFirstLaunch(firstLaunch)
        }
    }

    fun getOrderById(searchId: Int): Task {
        orderArrayList.forEach {
            if (it.id == searchId) {
                return it
            }
        }

        return orderArrayList.first()
    }

    val readFirstLaunch = dataStoreRepository.readFirstLaunch.asLiveData()
}