package com.example.trucksload.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.trucksload.R
import com.example.trucksload.util.Permissions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Permissions.hasLocationPermission(this)) {
            findNavController(R.id.navHostFragment).navigate(R.id.action_permissionFragment_to_loginScreenFragment)
        }


    }
}