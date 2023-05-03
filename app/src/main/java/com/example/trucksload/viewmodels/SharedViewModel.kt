package com.example.trucksload.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.trucksload.data.DataStoreRepository
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

    var chipSubstitute: Boolean = false
    var chipLoading: Boolean = false
    var chipPutAway: Boolean = false

    fun saveFirstLaunch(firstLaunch: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveFirstLaunch(firstLaunch)
        }
    }
    val readFirstLaunch = dataStoreRepository.readFirstLaunch.asLiveData()
}