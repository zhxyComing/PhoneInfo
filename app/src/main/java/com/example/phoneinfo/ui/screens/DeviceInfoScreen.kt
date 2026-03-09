package com.example.phoneinfo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.phoneinfo.data.DeviceInfo
import com.example.phoneinfo.data.DeviceInfoRepository
import com.example.phoneinfo.ui.components.InfoCard
import com.example.phoneinfo.ui.components.InfoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceInfoScreen(
    deviceInfo: DeviceInfo,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Device Info",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Device Basic Info
            item {
                InfoCard(title = "📱 Device Information") {
                    Column {
                        InfoItem(label = "Device", value = deviceInfo.deviceName)
                        InfoItem(label = "Model", value = deviceInfo.model)
                        InfoItem(label = "Manufacturer", value = deviceInfo.manufacturer)
                        InfoItem(label = "Brand", value = deviceInfo.brand)
                    }
                }
            }

            // Android System
            item {
                InfoCard(title = "🤖 Android System") {
                    Column {
                        InfoItem(label = "Android Version", value = deviceInfo.androidVersion)
                        InfoItem(label = "SDK Level", value = deviceInfo.sdkVersion.toString())
                        InfoItem(label = "Security Patch", value = deviceInfo.securityPatch)
                        InfoItem(label = "Build ID", value = deviceInfo.buildId)
                        InfoItem(label = "Display", value = deviceInfo.display)
                    }
                }
            }

            // Hardware
            item {
                InfoCard(title = "⚙️ Hardware") {
                    Column {
                        InfoItem(label = "Hardware", value = deviceInfo.hardware)
                        InfoItem(label = "Board", value = deviceInfo.board)
                        InfoItem(label = "Bootloader", value = deviceInfo.bootloader)
                        InfoItem(label = "CPU ABI", value = deviceInfo.cpuAbi)
                        InfoItem(label = "CPU Cores", value = deviceInfo.cpuCores.toString())
                    }
                }
            }

            // Screen
            item {
                InfoCard(title = "🖥️ Display") {
                    Column {
                        InfoItem(label = "Resolution", value = deviceInfo.screenResolution)
                        InfoItem(label = "Density", value = deviceInfo.screenDensity)
                        InfoItem(label = "Size", value = deviceInfo.screenSize)
                    }
                }
            }

            // Memory & Storage
            item {
                InfoCard(title = "💾 Memory & Storage") {
                    Column {
                        InfoItem(label = "Total RAM", value = deviceInfo.totalMemory)
                        InfoItem(label = "Available RAM", value = deviceInfo.availableMemory)
                        InfoItem(label = "Total Storage", value = deviceInfo.totalStorage)
                        InfoItem(label = "Available Storage", value = deviceInfo.availableStorage)
                    }
                }
            }

            // Battery
            item {
                InfoCard(title = "🔋 Battery") {
                    Column {
                        InfoItem(label = "Level", value = "${deviceInfo.batteryLevel}%")
                        InfoItem(label = "Status", value = deviceInfo.batteryStatus)
                        InfoItem(label = "Health", value = deviceInfo.batteryHealth)
                        InfoItem(label = "Technology", value = deviceInfo.batteryTechnology)
                        InfoItem(label = "Charging", value = if (deviceInfo.isCharging) "Yes" else "No")
                    }
                }
            }

            // Network
            item {
                InfoCard(title = "📡 Network") {
                    Column {
                        InfoItem(label = "Network Type", value = deviceInfo.networkType)
                        InfoItem(label = "Operator", value = deviceInfo.operatorName)
                        InfoItem(label = "SIM Cards", value = deviceInfo.simCount.toString())
                        InfoItem(label = "WiFi", value = if (deviceInfo.isWifiEnabled) "Enabled" else "Disabled")
                        InfoItem(label = "Bluetooth", value = deviceInfo.isBluetoothEnabled)
                    }
                }
            }

            // System
            item {
                InfoCard(title = "🕐 System") {
                    Column {
                        InfoItem(label = "Location", value = if (deviceInfo.isLocationEnabled) "Enabled" else "Disabled")
                        InfoItem(label = "Locale", value = deviceInfo.locale)
                        InfoItem(label = "Time Zone", value = deviceInfo.timeZone)
                        InfoItem(label = "Uptime", value = deviceInfo.uptime)
                        InfoItem(label = "Kernel", value = deviceInfo.kernelVersion)
                    }
                }
            }

            // Build Fingerprint
            item {
                InfoCard(title = "🔑 Build Fingerprint") {
                    Text(
                        text = deviceInfo.fingerprint,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
