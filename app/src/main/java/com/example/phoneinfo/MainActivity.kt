package com.example.phoneinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.phoneinfo.data.DeviceInfoRepository
import com.example.phoneinfo.ui.screens.DeviceInfoScreen
import com.example.phoneinfo.ui.theme.PhoneInfoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val deviceInfoRepository = DeviceInfoRepository(applicationContext)
        val deviceInfo = deviceInfoRepository.getDeviceInfo()
        
        setContent {
            PhoneInfoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DeviceInfoScreen(
                        deviceInfo = deviceInfo
                    )
                }
            }
        }
    }
}
