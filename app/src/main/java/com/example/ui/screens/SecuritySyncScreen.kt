package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.security.EncryptionManager
import com.example.ui.theme.*
import com.example.ui.viewmodel.PantryUiState
import com.example.ui.viewmodel.PantryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SecuritySyncScreen(
    uiState: PantryUiState,
    viewModel: PantryViewModel,
    modifier: Modifier = Modifier
) {
    val isBiometricEnabled by viewModel.biometricManager.isBiometricEnabled.collectAsState()
    val savedPin by viewModel.biometricManager.savedPin.collectAsState()
    var showChangePinDialog by remember { mutableStateOf(false) }
    var showCiphertextModal by remember { mutableStateOf(false) }
    val lang = uiState.selectedLanguage.lowercase()

    val biometricTitle = when (lang) {
        "de" -> "Biometrische Authentifizierung"
        "it" -> "Autenticazione biometrica"
        "pt" -> "Autenticação biométrica"
        else -> "Biometric Authentication"
    }

    val biometricSubtitle = when (lang) {
        "de" -> "Fingerabdruck, Face Unlock & Haupt-PIN"
        "it" -> "Impronta digitale, sblocco facciale e PIN principale"
        "pt" -> "Impressão digital, reconhecimento facial e PIN mestre"
        else -> "Fingerprint, Face Unlock & Master PIN"
    }

    val statusLabel = when (lang) {
        "de" -> "Aktueller Status"
        "it" -> "Stato attuale"
        "pt" -> "Status atual"
        else -> "Current Status"
    }

    val unlockedLabel = when (lang) {
        "de" -> "Authentifiziert & entsperrt"
        "it" -> "Autenticato e sbloccato"
        "pt" -> "Autenticado e desbloqueado"
        else -> "Authenticated & Unlocked"
    }

    val lockAppButton = when (lang) {
        "de" -> "App jetzt sperren"
        "it" -> "Blocca app ora"
        "pt" -> "Bloquear app agora"
        else -> "Lock App Now"
    }

    val pinLabel = when (lang) {
        "de" -> "Haushalts-Fallback-PIN"
        "it" -> "PIN di riserva della famiglia"
        "pt" -> "PIN de contingência da família"
        else -> "Household Fallback PIN"
    }

    val defaultText = when (lang) {
        "de" -> "Standard"
        "it" -> "Predefinito"
        "pt" -> "Padrão"
        else -> "Default"
    }

    val changePinButton = when (lang) {
        "de" -> "PIN ändern"
        "it" -> "Cambia PIN"
        "pt" -> "Alterar PIN"
        else -> "Change PIN"
    }

    val e2eeTitle = when (lang) {
        "de" -> "Ende-zu-Ende-Verschlüsselung (E2EE)"
        "it" -> "Crittografia end-to-end (E2EE)"
        "pt" -> "Criptografia ponta a ponta (E2EE)"
        else -> "End-to-End Encryption (E2EE)"
    }

    val e2eeSubtitle = when (lang) {
        "de" -> "Zero-Knowledge-Datenschutz auf der Oracle 26AI Cloud"
        "it" -> "Privacy zero-knowledge su cloud Oracle 26AI"
        "pt" -> "Privacidade de conhecimento zero no Oracle 26AI Cloud"
        else -> "Zero-knowledge privacy on Oracle 26AI Cloud"
    }

    val keyFingerprintLabel = when (lang) {
        "de" -> "Aktiver Hauptschlüssel-Fingerabdruck:"
        "it" -> "Impronta chiave principale attiva:"
        "pt" -> "Impressão digital da chave mestra ativa:"
        else -> "Active Master Key Fingerprint:"
    }

    val e2eeExplanation = when (lang) {
        "de" -> "Alle Vorratsartikel, Einkaufslisten und Familiennotizen werden vor der Übertragung lokal verschlüsselt. Oracle 26AI Cloud kann keine Klartextdaten lesen."
        "it" -> "Tutti gli articoli della dispensa, i registri della spesa e le note della famiglia sono crittografati localmente prima dell'invio. Oracle 26AI Cloud non può leggere dati in chiaro."
        "pt" -> "Todos os itens da despensa, registros de compras e notas da família são criptografados localmente antes do envio. O Oracle 26AI Cloud não tem acesso a dados em texto simples."
        else -> "All grocery inventory items, shopping records, and family notes are encrypted locally before transmission. Oracle 26AI Cloud cannot read plaintext records."
    }

    val rotateKeyButton = when (lang) {
        "de" -> "Schlüssel rotieren"
        "it" -> "Ruota chiave"
        "pt" -> "Rotacionar chave"
        else -> "Rotate Key"
    }

    val inspectCiphertextButton = when (lang) {
        "de" -> "Geheimtext prüfen"
        "it" -> "Ispeziona testo cifrato"
        "pt" -> "Inspecionar texto cifrado"
        else -> "Inspect Ciphertext"
    }

    val auditLogTitle = when (lang) {
        "de" -> "Oracle 26AI Cloud Transaktions-Prüfprotokoll"
        "it" -> "Registro di audit delle transazioni cloud Oracle 26AI"
        "pt" -> "Registro de auditoria de transações na nuvem Oracle 26AI"
        else -> "Oracle 26AI Cloud Transaction Audit Log"
    }

    val emptyLogsText = when (lang) {
        "de" -> "Noch keine Sync-Ereignisse aufgezeichnet. Tippe im Oracle 26AI-Tab auf 'Jetzt synchronisieren'."
        "it" -> "Nessun evento di sincronizzazione registrato. Tocca 'Sincronizza ora' nella scheda Oracle 26AI."
        "pt" -> "Nenhum evento de sincronização registrado ainda. Toque em 'Sincronizar agora' na guia Oracle 26AI."
        else -> "No sync events recorded yet. Tap 'Sync Now' in Oracle 26AI tab."
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("security_sync_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Biometric Authentication Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Fingerprint,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                            Column {
                                Text(
                                    biometricTitle,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    biometricSubtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Switch(
                            checked = isBiometricEnabled,
                            onCheckedChange = { viewModel.biometricManager.setBiometricEnabled(it) }
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(statusLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            Text(
                                unlockedLabel,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = StatusGood
                            )
                        }
                        Button(
                            onClick = { viewModel.lockApp() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = StatusCritical, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(lockAppButton, color = StatusCritical)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(pinLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            Text("•••• ($defaultText: $savedPin)", style = MaterialTheme.typography.bodyMedium)
                        }
                        OutlinedButton(
                            onClick = { showChangePinDialog = true },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(changePinButton)
                        }
                    }
                }
            }
        }

        // End-to-End Encryption (E2EE) Vault Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Security,
                                contentDescription = null,
                                tint = EmeraldPrimaryLight,
                                modifier = Modifier.size(28.dp)
                            )
                            Column {
                                Text(
                                    e2eeTitle,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    e2eeSubtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Surface(
                            color = EmeraldPrimaryContainerLight,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "AES-256-GCM",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                color = EmeraldOnPrimaryContainerLight
                            )
                        }
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(keyFingerprintLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            Text(
                                EncryptionManager.activeKeyFingerprint,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                e2eeExplanation,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FilledTonalButton(
                            onClick = {
                                viewModel.rotateEncryptionKey()
                                showCiphertextModal = true
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(rotateKeyButton, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                if (uiState.inspectedCiphertext == null) {
                                    viewModel.rotateEncryptionKey()
                                }
                                showCiphertextModal = true
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(inspectCiphertextButton, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Oracle 26AI Cloud Sync Audit Trail
        item {
            Text(
                auditLogTitle,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (uiState.syncLogs.isEmpty()) {
            item {
                Text(
                    emptyLogsText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        } else {
            items(uiState.syncLogs.take(10), key = { it.id }) { log ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                log.action,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(log.timestampEpoch)),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Surface(
                                color = if (log.status == "SUCCESS") EmeraldPrimaryContainerLight else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    log.status,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (log.status == "SUCCESS") EmeraldOnPrimaryContainerLight else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                            Text(
                                "${log.latencyMs}ms • ${log.recordsCount} records",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }
        }
    }

    // Change PIN Dialog
    if (showChangePinDialog) {
        var newPin by remember { mutableStateOf("") }
        val dialogTitle = when (lang) {
            "de" -> "Haushalts-Master-PIN ändern"
            "it" -> "Modifica PIN principale della famiglia"
            "pt" -> "Alterar PIN mestre da família"
            else -> "Change Household Master PIN"
        }
        val labelText = when (lang) {
            "de" -> "Neue PIN (4-6 Ziffern)"
            "it" -> "Nuovo PIN (4-6 cifre)"
            "pt" -> "Novo PIN (4-6 dígitos)"
            else -> "New PIN (4-6 digits)"
        }
        val saveButtonText = when (lang) {
            "de" -> "PIN speichern"
            "it" -> "Salva PIN"
            "pt" -> "Salvar PIN"
            else -> "Save PIN"
        }
        val cancelButtonText = when (lang) {
            "de" -> "Abbrechen"
            "it" -> "Annulla"
            "pt" -> "Cancelar"
            else -> "Cancel"
        }

        AlertDialog(
            onDismissRequest = { showChangePinDialog = false },
            title = { Text(dialogTitle) },
            text = {
                OutlinedTextField(
                    value = newPin,
                    onValueChange = { if (it.length <= 6) newPin = it },
                    label = { Text(labelText) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPin.length in 4..6) {
                            viewModel.biometricManager.updatePin(newPin)
                            showChangePinDialog = false
                        }
                    },
                    enabled = newPin.length in 4..6
                ) {
                    Text(saveButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePinDialog = false }) {
                    Text(cancelButtonText)
                }
            }
        )
    }

    // Ciphertext Inspection Modal
    if (showCiphertextModal && uiState.inspectedCiphertext != null) {
        val insp = uiState.inspectedCiphertext
        val modalTitle = when (lang) {
            "de" -> "E2EE Kryptografische Nutzlast-Prüfung"
            "it" -> "Ispezione payload crittografico E2EE"
            "pt" -> "Inspeção de carga criptográfica E2EE"
            else -> "E2EE Cryptographic Payload Inspection"
        }
        val modalDesc = when (lang) {
            "de" -> "Dieser rohe Geheimtext wird an die Oracle 26AI Cloud gesendet. Daten sind mit AES-256-GCM geschützt:"
            "it" -> "Questo testo cifrato grezzo è ciò che riceve il cloud Oracle 26AI. I dati sono protetti con AES-256-GCM:"
            "pt" -> "Este texto cifrado bruto é o que o Oracle 26AI Cloud recebe. Os dados são protegidos com AES-256-GCM:"
            else -> "This raw ciphertext is what Oracle 26AI Cloud receives. Data is protected with AES-256-GCM:"
        }
        val doneText = when (lang) {
            "de" -> "Fertig"
            "it" -> "Fatto"
            "pt" -> "Concluído"
            else -> "Done"
        }

        AlertDialog(
            onDismissRequest = { showCiphertextModal = false },
            title = { Text(modalTitle) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modalDesc,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Algorithm: ${insp.algorithm}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            Text("Key Fingerprint: ${insp.keyFingerprint}", style = MaterialTheme.typography.labelSmall)
                            Text("IV (12 bytes Hex):", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            Text(insp.ivHex, style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace, fontSize = 10.sp))
                            Text("Auth Tag (16 bytes Hex):", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            Text(insp.authTagHex, style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace, fontSize = 10.sp))
                            Text("Total Encrypted Size: ${insp.totalBytes} bytes", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showCiphertextModal = false }) {
                    Text(doneText)
                }
            }
        )
    }
}
