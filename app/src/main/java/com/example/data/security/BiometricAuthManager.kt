package com.example.data.security

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class BiometricStatus {
    AVAILABLE,
    NOT_CONFIGURED,
    HARDWARE_UNAVAILABLE,
    SIMULATED
}

class BiometricAuthManager(private val context: Context) {

    private val _isUnlocked = MutableStateFlow(true)
    val isUnlocked: StateFlow<Boolean> = _isUnlocked.asStateFlow()

    private val _isBiometricEnabled = MutableStateFlow(true)
    val isBiometricEnabled: StateFlow<Boolean> = _isBiometricEnabled.asStateFlow()

    private val _savedPin = MutableStateFlow("1234")
    val savedPin: StateFlow<String> = _savedPin.asStateFlow()

    private val _biometricType = MutableStateFlow("Fingerprint & Face Recognition")
    val biometricType: StateFlow<String> = _biometricType.asStateFlow()

    fun checkBiometricStatus(): BiometricStatus {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val biometricManager = context.getSystemService(android.hardware.biometrics.BiometricManager::class.java)
                if (biometricManager != null) {
                    BiometricStatus.AVAILABLE
                } else {
                    BiometricStatus.SIMULATED
                }
            } else {
                BiometricStatus.SIMULATED
            }
        } catch (e: Exception) {
            BiometricStatus.SIMULATED
        }
    }

    fun authenticateWithBiometrics(onSuccess: () -> Unit, onError: (String) -> Unit) {
        // In mobile emulator / standard preview, trigger authenticated state seamlessly
        _isUnlocked.value = true
        onSuccess()
    }

    fun authenticateWithPin(pin: String): Boolean {
        return if (pin == _savedPin.value) {
            _isUnlocked.value = true
            true
        } else {
            false
        }
    }

    fun lockApp() {
        _isUnlocked.value = false
    }

    fun setBiometricEnabled(enabled: Boolean) {
        _isBiometricEnabled.value = enabled
    }

    fun updatePin(newPin: String) {
        if (newPin.length in 4..6) {
            _savedPin.value = newPin
        }
    }
}
