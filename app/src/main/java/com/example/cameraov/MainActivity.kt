package com.example.cameraov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cameraov.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging

private const val KEY_START_DESTINATION = "showDefaultDisplay"

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w("osmovia", "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }
            Log.d("osmovia", "Token: ${task.result}")
        })
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val navHost =
            supportFragmentManager.findFragmentById(R.id.containerFragment) as NavHostFragment
        val navController = navHost.navController
    
        val navGraph = navController.navInflater.inflate(R.navigation.tabs_navigation)
        
        if (intent.extras?.getBoolean(KEY_START_DESTINATION) != false) {
            navGraph.setStartDestination(R.id.camera_navigation)
        } else {
            navGraph.setStartDestination(R.id.photo_navigation)
        }
        navController.graph = navGraph
        
        findViewById<BottomNavigationView>(R.id.bottomBarId)
            .setupWithNavController(navController)
    }
}
