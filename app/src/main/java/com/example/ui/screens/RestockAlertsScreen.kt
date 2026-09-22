package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.RestockAlertEntity
import com.example.ui.components.UrgencyBadge
import com.example.ui.i18n.LocalizationManager
import com.example.ui.theme.*
import com.example.ui.viewmodel.PantryUiState
import com.example.ui.viewmodel.PantryViewModel

@Composable
fun RestockAlertsScreen(
    uiState: PantryUiState,
    viewModel: PantryViewModel,
    modifier: Modifier = Modifier
) {
    val strings = LocalizationManager.getStrings(uiState.selectedLanguage)
    val alerts = uiState.restockAlerts
    val criticalCount = alerts.count { it.urgency == "CRITICAL_OUT_OF_STOCK" }
    val lowCount = alerts.count { it.urgency == "LOW_STOCK" }
    val expiringCount = alerts.count { it.urgency == "EXPIRING_SOON" }

    val radarSubtitle = when (uiState.selectedLanguage.lowercase()) {
        "de" -> "Kontinuierlicher Inventarscan & Schwellenwertalarme"
        "it" -> "Scansione continua dell'inventario e avvisi di soglia"
        "pt" -> "Varredura contínua de estoque e alertas de limite"
        else -> "Continuous inventory scan & threshold alerts"
    }

    val activeWord = when (uiState.selectedLanguage.lowercase()) {
        "de" -> "Aktiv"
        "it" -> "Attivi"
        "pt" -> "Ativos"
        else -> "Active"
    }

    val criticalOutLabel = when (uiState.selectedLanguage.lowercase()) {
        "de" -> "Kritisch leer"
        "it" -> "Critici"
        "pt" -> "Críticos"
        else -> "Critical Out"
    }

    val expiringSoonLabel = when (uiState.selectedLanguage.lowercase()) {
        "de" -> "Läuft bald ab"
        "it" -> "In scadenza"
        "pt" -> "Vencendo logo"
        else -> "Expiring Soon"
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("restock_alerts_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Summary Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (alerts.isNotEmpty()) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = strings.alertsHeroTitle,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = radarSubtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Stat Breakdown
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(criticalOutLabel, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = StatusCritical)
                                Text("$criticalCount", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = StatusCritical)
                            }
                        }
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(strings.lowStock, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = AmberSecondaryLight)
                                Text("$lowCount", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = AmberSecondaryLight)
                            }
                        }
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(expiringSoonLabel, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = OracleTertiaryLight)
                                Text("$expiringCount", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = OracleTertiaryLight)
                            }
                        }
                    }

                    // Quick 1-tap "Add All to Shopping List"
                    if (alerts.isNotEmpty()) {
                        Button(
                            onClick = { viewModel.addAllCriticalAlertsToShopping() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.PlaylistAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.addToShoppingList)
                        }
                    }
                }
            }
        }

        // Alert Items
        if (alerts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp),
                            tint = StatusGood
                        )
                        Text(
                            strings.allGoodTitle,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            strings.allGoodSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        } else {
            items(alerts, key = { it.id }) { alert ->
                RestockAlertCard(
                    alert = alert,
                    languageCode = uiState.selectedLanguage,
                    onAddToShopping = { viewModel.addAlertToShopping(alert) },
                    onDismiss = { viewModel.dismissAlert(alert) }
                )
            }
        }
    }
}

@Composable
fun RestockAlertCard(
    alert: RestockAlertEntity,
    languageCode: String,
    onAddToShopping: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalizationManager.getStrings(languageCode)
    val displayName = LocalizationManager.localizeItemName(alert.itemName, languageCode)
    val displayCategory = LocalizationManager.localizeCategory(alert.category, languageCode)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("restock_alert_${alert.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = displayCategory,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                UrgencyBadge(urgency = alert.urgency)
            }

            // Message
            Text(
                text = alert.alertMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Stock Comparison Bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "${strings.quantity}: ${alert.currentQuantity} ${alert.unit}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${strings.threshold} ${alert.thresholdQuantity} ${alert.unit}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                val fraction = if (alert.thresholdQuantity > 0) {
                    (alert.currentQuantity / alert.thresholdQuantity).toFloat().coerceIn(0f, 1f)
                } else 0f
                LinearProgressIndicator(
                    progress = { fraction },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = if (alert.urgency == "CRITICAL_OUT_OF_STOCK") StatusCritical else AmberSecondaryLight,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismiss) {
                    Text(strings.dismiss, color = MaterialTheme.colorScheme.outline)
                }
                Spacer(modifier = Modifier.width(6.dp))
                FilledTonalButton(
                    onClick = onAddToShopping,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        Icons.Default.AddShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(strings.addShoppingItem)
                }
            }
        }
    }
}
