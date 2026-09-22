package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.PantryViewModel

@Composable
fun BiometricLockScreen(
    viewModel: PantryViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var pin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }
    val lang = uiState.selectedLanguage.lowercase()

    val vaultTitle = when (lang) {
        "de" -> "Vorratsinventar"
        "it" -> "Inventario dispensa"
        "pt" -> "Inventário da despensa"
        else -> "Pantry Inventory"
    }

    val vaultSubtitle = when (lang) {
        "de" -> "Haushaltstresor • Biometrie & AES-256 E2EE"
        "it" -> "Cassaforte di casa • Biometria e E2EE AES-256"
        "pt" -> "Cofre familiar • Biometria e E2EE AES-256"
        else -> "Household Vault • Biometric & AES-256 E2EE"
    }

    val unlockBiometricButton = when (lang) {
        "de" -> "Mit Biometrie entsperren"
        "it" -> "Sblocca con biometria"
        "pt" -> "Desbloquear com biometria"
        else -> "Unlock with Biometrics"
    }

    val incorrectPinText = when (lang) {
        "de" -> "Falsche PIN. (Standard: 1234)"
        "it" -> "PIN non corretto. (Predefinito: 1234)"
        "pt" -> "PIN incorreto. (Padrão: 1234)"
        else -> "Incorrect PIN. (Default: 1234)"
    }

    val demoPinText = when (lang) {
        "de" -> "Demo-Fallback-PIN: 1234"
        "it" -> "PIN di riserva demo: 1234"
        "pt" -> "PIN de contingência demo: 1234"
        else -> "Demo Fallback PIN: 1234"
    }

    val clearKeyText = when (lang) {
        "de" -> "LÖSCH"
        "it" -> "CANC"
        "pt" -> "LIMP"
        else -> "CLEAR"
    }

    val infiniteTransition = rememberInfiniteTransition(label = "fingerprint_glow")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Surface(
        modifier = modifier
            .fillMaxSize()
            .testTag("biometric_lock_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = EmeraldPrimaryLight.copy(alpha = 0.15f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = EmeraldPrimaryLight,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Text(
                    text = vaultTitle,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = vaultSubtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Biometric Fingerprint Pulse
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    EmeraldPrimaryLight.copy(alpha = 0.25f),
                                    Color.Transparent
                                )
                            )
                        )
                        .border(2.dp, EmeraldPrimaryLight.copy(alpha = 0.5f), CircleShape)
                        .clickable { viewModel.unlockWithBiometrics() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Fingerprint,
                        contentDescription = "Tap for Biometric Unlock",
                        tint = EmeraldPrimaryLight,
                        modifier = Modifier.size(52.dp)
                    )
                }

                Button(
                    onClick = { viewModel.unlockWithBiometrics() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("btn_biometric_unlock")
                ) {
                    Icon(Icons.Default.Fingerprint, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(unlockBiometricButton)
                }

                // PIN Input Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    for (i in 0 until 4) {
                        val isFilled = i < pin.length
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isFilled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), CircleShape)
                        )
                    }
                }

                if (pinError != null) {
                    Text(
                        pinError ?: "",
                        color = StatusCritical,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Numeric Keypad
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                val rows = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf(clearKeyText, "0", "DEL")
                )

                rows.forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        row.forEach { key ->
                            Surface(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        when (key) {
                                            clearKeyText -> {
                                                pin = ""
                                                pinError = null
                                            }
                                            "DEL" -> {
                                                if (pin.isNotEmpty()) pin = pin.dropLast(1)
                                                pinError = null
                                            }
                                            else -> {
                                                if (pin.length < 4) {
                                                    pin += key
                                                    pinError = null
                                                    if (pin.length == 4) {
                                                        val ok = viewModel.biometricManager.authenticateWithPin(pin)
                                                        if (!ok) {
                                                            pinError = incorrectPinText
                                                            pin = ""
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    },
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    if (key == "DEL") {
                                        Icon(Icons.Default.Backspace, contentDescription = "Delete", modifier = Modifier.size(20.dp))
                                    } else {
                                        Text(
                                            text = key,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = if (key == clearKeyText) 10.sp else 18.sp
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Text(
                    text = demoPinText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

