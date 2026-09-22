package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.oracle.CloudConnectionState
import com.example.data.oracle.DepletionForecast
import com.example.data.oracle.OracleConnectionConfig
import com.example.data.oracle.WasteRiskItem
import com.example.ui.components.OracleCloudStatusBadge
import com.example.ui.i18n.LocalizationManager
import com.example.ui.theme.*
import com.example.ui.viewmodel.PantryUiState
import com.example.ui.viewmodel.PantryViewModel

@Composable
fun OracleAnalyticsScreen(
    uiState: PantryUiState,
    viewModel: PantryViewModel,
    modifier: Modifier = Modifier
) {
    val report = uiState.analyticsReport
    val lang = uiState.selectedLanguage.lowercase()

    val autonomousDbTitle = when (lang) {
        "de" -> "Autonome DB"
        "it" -> "DB Autonomo"
        "pt" -> "BD Autônomo"
        else -> "Autonomous DB"
    }

    val subtitle = when (lang) {
        "de" -> "Nahtlose Cloud-Synchronisierung, KI-Vektorsuche und prädiktive Verbrauchsmodellierung mit Ende-zu-Ende-Verschlüsselung."
        "it" -> "Sincronizzazione cloud trasparente, ricerca vettoriale AI e modelli predittivi di consumo con crittografia end-to-end."
        "pt" -> "Sincronização na nuvem contínua, busca vetorial por IA e modelagem preditiva de consumo com criptografia ponta a ponta."
        else -> "Seamless cloud synchronization, AI Vector search, and consumption predictive modeling with end-to-end encryption."
    }

    val syncStatusLabel = when (lang) {
        "de" -> "Sync-Status:"
        "it" -> "Stato sincronizzazione:"
        "pt" -> "Status de sincronização:"
        else -> "Sync Status:"
    }

    val storageLayerLabel = when (lang) {
        "de" -> "Speicherschicht:"
        "it" -> "Livello di archiviazione:"
        "pt" -> "Camada de armazenamento:"
        else -> "Storage Layer:"
    }

    val storageLayerValue = when (lang) {
        "de" -> if (uiState.isOfflineMode) "Lokales Room SQLite (Offline-Cache)" else "Oracle 26AI Cloud + Room Cache"
        "it" -> if (uiState.isOfflineMode) "Room SQLite locale (cache offline)" else "Cloud Oracle 26AI + cache Room"
        "pt" -> if (uiState.isOfflineMode) "Room SQLite local (cache offline)" else "Oracle 26AI Cloud + cache Room"
        else -> if (uiState.isOfflineMode) "Local Room SQLite (Offline Cache)" else "Oracle 26AI Cloud + Room Cache"
    }

    val syncButtonText = when (lang) {
        "de" -> if (uiState.isSyncing) "Synchronisieren..." else "Jetzt synchronisieren"
        "it" -> if (uiState.isSyncing) "Sincronizzazione..." else "Sincronizza ora"
        "pt" -> if (uiState.isSyncing) "Sincronizando..." else "Sincronizar agora"
        else -> if (uiState.isSyncing) "Syncing..." else "Sync Now"
    }

    val offlineButtonText = when (lang) {
        "de" -> if (uiState.isOfflineMode) "Online gehen" else "Offline testen"
        "it" -> if (uiState.isOfflineMode) "Vai online" else "Prova offline"
        "pt" -> if (uiState.isOfflineMode) "Ficar online" else "Testar offline"
        else -> if (uiState.isOfflineMode) "Go Online" else "Test Offline"
    }

    val healthTitle = when (lang) {
        "de" -> "Vorrat-Gesundheit"
        "it" -> "Salute dispensa"
        "pt" -> "Saúde da despensa"
        else -> "Pantry Health"
    }

    val healthDesc = when (lang) {
        "de" -> "Basierend auf Vorratsbalance"
        "it" -> "In base al bilancio scorte"
        "pt" -> "Equilíbrio do estoque"
        else -> "Based on stock balance"
    }

    val wasteTitle = when (lang) {
        "de" -> "Abfallvermeidung"
        "it" -> "Prevenzione sprechi"
        "pt" -> "Prevenção desperdício"
        else -> "Waste Prevention"
    }

    val wasteDesc = when (lang) {
        "de" -> "Frische-Optimierung"
        "it" -> "Ottimizzazione freschezza"
        "pt" -> "Otimização de frescor"
        else -> "Freshness optimization"
    }

    val spendTitle = when (lang) {
        "de" -> "Gesch. Ausgaben"
        "it" -> "Spesa stimata"
        "pt" -> "Gasto estimado"
        else -> "Est. Spend"
    }

    val spendDesc = when (lang) {
        "de" -> "Prognose / Monat"
        "it" -> "Previsione / mese"
        "pt" -> "Previsão / mês"
        else -> "Forecast / month"
    }

    val depletionHeader = when (lang) {
        "de" -> "Oracle 26AI Verbrauchsprognosen"
        "it" -> "Previsioni esaurimento scorte Oracle 26AI"
        "pt" -> "Previsões de consumo Oracle 26AI"
        else -> "Oracle 26AI Depletion Forecasts"
    }

    val depletionSubtitle = when (lang) {
        "de" -> "Machine-Learning-Schätzungen basierend auf der Haushaltsverbrauchsgeschwindigkeit"
        "it" -> "Stime di esaurimento basate sul consumo effettivo della famiglia"
        "pt" -> "Estimativas baseadas no ritmo de consumo da família"
        else -> "Machine learning burn-down estimates based on household consumption velocity"
    }

    val wasteRadarHeader = when (lang) {
        "de" -> "Lebensmittelverschwendungs-Radar"
        "it" -> "Radar prevenzione spreco alimentare"
        "pt" -> "Radar de prevenção de desperdício"
        else -> "Food Waste Prevention Radar"
    }

    val wasteRadarSubtitle = when (lang) {
        "de" -> "Artikel nahe dem Verfallsdatum mit KI-Rezeptvorschlägen zur Müllvermeidung"
        "it" -> "Articoli vicini alla scadenza con ricette consigliate dall'IA per evitare sprechi"
        "pt" -> "Itens próximos ao vencimento com sugestões de receitas por IA para evitar desperdício"
        else -> "Items approaching shelf life with AI recipe suggestions to avoid waste"
    }

    val velocityHeader = when (lang) {
        "de" -> "Haushalts-Umschlagsgeschwindigkeit nach Kategorien"
        "it" -> "Velocità di consumo per categoria della famiglia"
        "pt" -> "Velocidade de rotação por categoria da família"
        else -> "Household Category Turnover Velocity"
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("oracle_analytics_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Oracle 26AI Cloud Sync & Autonomous Database Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(20.dp)
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
                            Surface(
                                color = StatusOracleCloud,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "ORACLE 26ai",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        fontSize = 11.sp
                                    ),
                                    color = Color.White
                                )
                            }
                            Text(
                                autonomousDbTitle,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(onClick = { viewModel.setShowOracleConfigDialog(true) }) {
                            Icon(Icons.Default.Settings, contentDescription = "Config")
                        }
                    }

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Telemetry Status Bar
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(syncStatusLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                                OracleCloudStatusBadge(
                                    connectionState = uiState.connectionState,
                                    isOfflineMode = uiState.isOfflineMode
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(storageLayerLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                                Text(
                                    storageLayerValue,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    // Sync Controls & Offline Mode Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.triggerOracleSync() },
                            enabled = !uiState.isSyncing,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (uiState.isSyncing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(syncButtonText)
                            } else {
                                Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(syncButtonText)
                            }
                        }

                        FilledTonalButton(
                            onClick = { viewModel.toggleOfflineMode() },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                if (uiState.isOfflineMode) Icons.Default.CloudOff else Icons.Default.CloudQueue,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(offlineButtonText)
                        }
                    }

                    // Last Sync Result Toast/Banner
                    uiState.lastSyncResult?.let { res ->
                        Surface(
                            color = if (res.success) EmeraldPrimaryContainerLight.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    if (res.success) Icons.Default.CheckCircle else Icons.Default.Info,
                                    contentDescription = null,
                                    tint = if (res.success) StatusGood else MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = res.message,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Predictive Health & Waste Prevention Scores
        if (report != null) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(healthTitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            Text(
                                "${report.overallHealthScore}%",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = if (report.overallHealthScore >= 75) StatusGood else StatusWarning
                            )
                            Text(healthDesc, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.outline)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(wasteTitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            Text(
                                "${report.wastePreventionScore}%",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = OracleTertiaryLight
                            )
                            Text(wasteDesc, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.outline)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(spendTitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                            Text(
                                "${uiState.currencySymbol}${report.estimatedMonthlySpend.toInt()}",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = AmberSecondaryLight
                            )
                            Text(spendDesc, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }

            // Section: Depletion Forecasting
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        depletionHeader,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        depletionSubtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(report.depletionForecasts.take(6), key = { it.itemId }) { forecast ->
                DepletionForecastCard(forecast = forecast, languageCode = lang)
            }

            // Section: Food Waste Prevention Radar
            if (report.wasteRiskItems.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            wasteRadarHeader,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            wasteRadarSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                items(report.wasteRiskItems, key = { it.itemId }) { wasteItem ->
                    WasteRiskCard(item = wasteItem, languageCode = lang)
                }
            }

            // Category Velocity Breakdown
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            velocityHeader,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        val turnoverSuffix = when (lang) {
                            "de" -> "Tage Umschlag"
                            "it" -> "giorni rotazione"
                            "pt" -> "dias de rotação"
                            else -> "days turnover"
                        }
                        val perWeekSuffix = when (lang) {
                            "de" -> "/Wo."
                            "it" -> "/sett."
                            "pt" -> "/sem."
                            else -> "/wk"
                        }
                        report.categoryStats.forEach { stat ->
                            val localizedCat = LocalizationManager.localizeCategory(stat.category, lang)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(localizedCat, style = MaterialTheme.typography.bodyMedium)
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            "${stat.averageTurnoverDays} $turnoverSuffix",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                                        )
                                    }
                                    Text(
                                        "${uiState.currencySymbol}${stat.weeklyExpenseEstimated}$perWeekSuffix",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = AmberSecondaryLight
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Oracle Config Dialog
    if (uiState.showOracleConfigDialog) {
        OracleConfigDialog(
            languageCode = lang,
            onDismiss = { viewModel.setShowOracleConfigDialog(false) },
            onSave = { config -> viewModel.updateOracleConfig(config) }
        )
    }
}

@Composable
fun DepletionForecastCard(forecast: DepletionForecast, languageCode: String = "en") {
    val localizedName = LocalizationManager.localizeItemName(forecast.itemName, languageCode)
    val localizedUnit = LocalizationManager.localizeUnit(forecast.unit, languageCode)
    val localizedRecommendation = LocalizationManager.localizeAlertReason(forecast.recommendation, languageCode)

    val confidenceLabel = when (languageCode) {
        "de" -> "Konfidenz:"
        "it" -> "Affidabilità:"
        "pt" -> "Confiança:"
        else -> "Confidence:"
    }

    val remainingLabel = when (languageCode) {
        "de" -> "übrig"
        "it" -> "rimanenti"
        "pt" -> "restantes"
        else -> "remaining"
    }

    val todayText = when (languageCode) {
        "de" -> "HEUTE"
        "it" -> "OGGI"
        "pt" -> "HOJE"
        else -> "TODAY"
    }

    val runoutLabel = when (languageCode) {
        "de" -> "Ablauf"
        "it" -> "esaurimento"
        "pt" -> "término"
        else -> "runout"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (forecast.isCritical) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    localizedName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    localizedRecommendation,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (forecast.isCritical) StatusCritical else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        "$confidenceLabel ${forecast.confidenceScore}%",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text("•", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.outline)
                    Text(
                        "${forecast.currentQuantity} $localizedUnit $remainingLabel",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Surface(
                color = if (forecast.isCritical) StatusCritical.copy(alpha = 0.15f) else MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        if (forecast.daysRemaining == 0) todayText else "${forecast.daysRemaining}d",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                        color = if (forecast.isCritical) StatusCritical else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        runoutLabel,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = if (forecast.isCritical) StatusCritical else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun WasteRiskCard(item: WasteRiskItem, languageCode: String = "en") {
    val localizedName = LocalizationManager.localizeItemName(item.itemName, languageCode)
    val daysLeftLabel = when (languageCode) {
        "de" -> "Tage übrig"
        "it" -> "giorni rimasti"
        "pt" -> "dias restantes"
        else -> "days left"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    localizedName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    color = if (item.riskLevel == "HIGH") StatusCritical.copy(alpha = 0.15f) else AmberSecondaryContainerLight,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        "${item.daysUntilExpiry} $daysLeftLabel",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (item.riskLevel == "HIGH") StatusCritical else AmberOnSecondaryContainerLight
                        )
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Outlined.RestaurantMenu,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    item.suggestedRecipeAction,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun OracleConfigDialog(
    languageCode: String = "en",
    onDismiss: () -> Unit,
    onSave: (OracleConnectionConfig) -> Unit
) {
    var endpoint by remember { mutableStateOf("https://adb.us-ashburn-1.oraclecloud.com/ords/pantry_ai/26ai/v1") }
    var dbName by remember { mutableStateOf("ORCL26AI_AUTONOMOUS_PANTRY") }
    var ocid by remember { mutableStateOf("ocid1.tenancy.oc1..pantry26aicloudstorage") }
    var enableVector by remember { mutableStateOf(true) }

    val dialogTitle = when (languageCode) {
        "de" -> "Oracle 26AI Cloud-Konfiguration"
        "it" -> "Configurazione cloud Oracle 26AI"
        "pt" -> "Configuração de nuvem Oracle 26AI"
        else -> "Oracle 26AI Cloud Configuration"
    }

    val saveText = when (languageCode) {
        "de" -> "Konfiguration speichern"
        "it" -> "Salva configurazione"
        "pt" -> "Salvar configuração"
        else -> "Save Configuration"
    }

    val cancelText = when (languageCode) {
        "de" -> "Abbrechen"
        "it" -> "Annulla"
        "pt" -> "Cancelar"
        else -> "Cancel"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialogTitle) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = endpoint,
                    onValueChange = { endpoint = it },
                    label = { Text("ORDS 26AI API Endpoint") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dbName,
                    onValueChange = { dbName = it },
                    label = { Text("Autonomous Database Service") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = ocid,
                    onValueChange = { ocid = it },
                    label = { Text("Cloud Tenancy OCID") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        when (languageCode) {
                            "de" -> "KI-Vektorsuchmaschine"
                            "it" -> "Motore di ricerca vettoriale AI"
                            "pt" -> "Mecanismo de busca vetorial por IA"
                            else -> "AI Vector Search Engine"
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Switch(checked = enableVector, onCheckedChange = { enableVector = it })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        OracleConnectionConfig(
                            endpointUrl = endpoint,
                            autonomousDbName = dbName,
                            tenancyOcid = ocid,
                            enableVectorAiEngine = enableVector
                        )
                    )
                }
            ) {
                Text(saveText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(cancelText)
            }
        }
    )
}

