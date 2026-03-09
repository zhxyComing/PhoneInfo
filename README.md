# PhoneInfo 📱

A modern Android application that displays comprehensive device information using Jetpack Compose.

## Overview

PhoneInfo is an Android app that collects and displays detailed information about your device, including hardware specifications, system details, battery status, network information, and more. Built with modern Android development practices using Jetpack Compose.

## Features

### Device Information
- Device name, model, manufacturer, and brand
- Android version and SDK level
- Security patch level
- Build ID and fingerprint
- Hardware details (CPU, board, bootloader)

### Display Information
- Screen resolution
- Screen density (LDPI to XXXHDPI)
- Screen size in inches

### Hardware Stats
- CPU architecture and core count
- Total and available memory
- Total and available storage

### Battery Information
- Battery level percentage
- Charging status
- Battery health
- Battery technology
- Charging state

### Network & Connectivity
- Network type (WiFi, Cellular, Ethernet, VPN)
- Operator name
- SIM card count
- WiFi enabled status
- Bluetooth status
- Location enabled status

### System Information
- Locale and language
- Time zone
- System uptime
- Kernel version

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Architecture**: MVVM (Model-View-ViewModel)
- **Build System**: Gradle with Kotlin DSL

## Project Structure

```
PhoneInfo/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/phoneinfo/
│   │   │   │   ├── MainActivity.kt          # Main entry point
│   │   │   │   ├── data/
│   │   │   │   │   ├── DeviceInfo.kt       # Data model
│   │   │   │   │   └── DeviceInfoRepository.kt  # Data repository
│   │   │   │   └── ui/
│   │   │   │       ├── screens/            # Compose screens
│   │   │   │       ├── components/         # Reusable components
│   │   │   │       └── theme/              # Material theme
│   │   │   └── res/                        # Resources
│   │   ├── test/                           # Unit tests
│   │   └── androidTest/                    # Instrumented tests
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- JDK 11 or higher
- Android SDK with API 36

### Building

1. Clone the repository:
   ```bash
   git clone https://github.com/zhxyComing/PhoneInfo.git
   ```

2. Open the project in Android Studio

3. Build the debug APK:
   ```bash
   ./gradlew assembleDebug
   ```

4. The APK will be generated at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

### Running

1. Connect your Android device or start an emulator
2. In Android Studio, click "Run" or press `Shift + F10`
3. The app will launch displaying all device information

## Permissions

The app requires the following permissions (Android 6.0+):

- **INTERNET** - For network-related information
- **ACCESS_NETWORK_STATE** - To check network connectivity
- **ACCESS_WIFI_STATE** - For WiFi status
- **BLUETOOTH** - To check Bluetooth status
- **ACCESS_FINE_LOCATION** / **ACCESS_COARSE_LOCATION** - For location status
- **READ_PHONE_STATE** - For telephony information

## Screenshots

The app displays information in a clean, card-based layout using Material Design 3.

## License

This project is licensed under the MIT License.

## Author

[zhxy](https://github.com/zhxyComing)
