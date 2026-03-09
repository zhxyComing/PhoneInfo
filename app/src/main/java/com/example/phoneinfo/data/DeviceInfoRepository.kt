package com.example.phoneinfo.data

import android.app.ActivityManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.SystemClock
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader
import java.util.Locale

class DeviceInfoRepository(private val context: Context) {

    fun getDeviceInfo(): DeviceInfo {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { iFilter ->
            context.registerReceiver(null, iFilter)
        }

        return DeviceInfo(
            deviceName = Build.DEVICE,
            model = Build.MODEL,
            manufacturer = Build.MANUFACTURER,
            brand = Build.BRAND,
            androidVersion = Build.VERSION.RELEASE,
            sdkVersion = Build.VERSION.SDK_INT,
            securityPatch = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Build.VERSION.SECURITY_PATCH else "N/A",
            buildId = Build.ID,
            hardware = Build.HARDWARE,
            board = Build.BOARD,
            bootloader = Build.BOOTLOADER,
            fingerprint = Build.FINGERPRINT,
            display = Build.DISPLAY,
            screenResolution = "${displayMetrics.widthPixels} x ${displayMetrics.heightPixels}",
            screenDensity = getDensityString(displayMetrics.densityDpi),
            screenSize = getScreenSizeInches(windowManager),
            cpuAbi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                "${Build.SUPPORTED_ABIS[0]}"
            } else {
                @Suppress("DEPRECATION")
                Build.CPU_ABI
            },
            cpuCores = Runtime.getRuntime().availableProcessors(),
            totalMemory = formatBytes(memoryInfo.totalMem),
            availableMemory = formatBytes(memoryInfo.availMem),
            totalStorage = getTotalStorage(),
            availableStorage = getAvailableStorage(),
            batteryLevel = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1,
            batteryStatus = getBatteryStatus(batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1),
            batteryHealth = getBatteryHealth(batteryStatus?.getIntExtra(BatteryManager.EXTRA_HEALTH, -1) ?: -1),
            batteryTechnology = batteryStatus?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Unknown",
            isCharging = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) == BatteryManager.BATTERY_STATUS_CHARGING,
            networkType = getNetworkType(connectivityManager),
            operatorName = telephonyManager.networkOperatorName ?: "Unknown",
            simCount = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) telephonyManager.activeModemCount else 1,
            isWifiEnabled = wifiManager.isWifiEnabled,
            isBluetoothEnabled = getBluetoothStatus(),
            isLocationEnabled = isLocationEnabled(),
            locale = Locale.getDefault().displayLanguage,
            timeZone = java.util.TimeZone.getDefault().id,
            uptime = formatUptime(SystemClock.elapsedRealtime()),
            kernelVersion = getKernelVersion()
        )
    }

    private fun getDensityString(densityDpi: Int): String {
        return when {
            densityDpi <= DisplayMetrics.DENSITY_LOW -> "LDPI ($densityDpi dpi)"
            densityDpi <= DisplayMetrics.DENSITY_MEDIUM -> "MDPI ($densityDpi dpi)"
            densityDpi <= DisplayMetrics.DENSITY_HIGH -> "HDPI ($densityDpi dpi)"
            densityDpi <= DisplayMetrics.DENSITY_XHIGH -> "XHDPI ($densityDpi dpi)"
            densityDpi <= DisplayMetrics.DENSITY_XXHIGH -> "XXHDPI ($densityDpi dpi)"
            else -> "XXXHDPI ($densityDpi dpi)"
        }
    }

    @Suppress("DEPRECATION")
    private fun getScreenSizeInches(windowManager: WindowManager): String {
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getRealMetrics(metrics)

        val widthInches = metrics.widthPixels / metrics.xdpi
        val heightInches = metrics.heightPixels / metrics.ydpi
        val diagonalInches = kotlin.math.sqrt(widthInches * widthInches + heightInches * heightInches)

        return String.format("%.2f inches", diagonalInches)
    }

    private fun formatBytes(bytes: Long): String {
        val gb = bytes / (1024.0 * 1024.0 * 1024.0)
        return String.format("%.2f GB", gb)
    }

    private fun getTotalStorage(): String {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return formatBytes(blockSize * totalBlocks)
    }

    private fun getAvailableStorage(): String {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return formatBytes(blockSize * availableBlocks)
    }

    private fun getBatteryStatus(status: Int): String {
        return when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
            BatteryManager.BATTERY_STATUS_FULL -> "Full"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Not Charging"
            else -> "Unknown"
        }
    }

    private fun getBatteryHealth(health: Int): String {
        return when (health) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Failure"
            BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
            else -> "Unknown"
        }
    }

    private fun getNetworkType(connectivityManager: ConnectivityManager): String {
        val network = connectivityManager.activeNetwork ?: return "Not Connected"
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return "Not Connected"

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN"
            else -> "Unknown"
        }
    }

    private fun getBluetoothStatus(): String {
        return try {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            when {
                bluetoothAdapter == null -> "Not Supported"
                bluetoothAdapter.isEnabled -> "Enabled"
                else -> "Disabled"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    private fun isLocationEnabled(): Boolean {
        return try {
            val locationMode = Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            locationMode != Settings.Secure.LOCATION_MODE_OFF
        } catch (e: Exception) {
            false
        }
    }

    private fun formatUptime(millis: Long): String {
        val seconds = millis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> "${days}d ${hours % 24}h ${minutes % 60}m"
            hours > 0 -> "${hours}h ${minutes % 60}m ${seconds % 60}s"
            minutes > 0 -> "${minutes}m ${seconds % 60}s"
            else -> "${seconds}s"
        }
    }

    private fun getKernelVersion(): String {
        return try {
            val process = Runtime.getRuntime().exec("uname -r")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val version = reader.readLine() ?: "Unknown"
            reader.close()
            version
        } catch (e: Exception) {
            Build.VERSION.SDK_INT.toString()
        }
    }
}
