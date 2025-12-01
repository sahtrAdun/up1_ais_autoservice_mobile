package com.autoservice.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.autoservice.mobile.ui.navigation.AppNavigation
import com.autoservice.mobile.ui.theme.AisAutoserviceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AisAutoserviceTheme {
                AppNavigation()
            }
        }
    }
}