package com.example.ui.screens

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.i18n.LocalizationManager
import com.example.ui.theme.*
import com.example.ui.viewmodel.PantryUiState
import com.example.ui.viewmodel.PantryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupRestoreScreen(
    uiState: PantryUiState,
    viewModel: PantryViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val strings = LocalizationManager.getStrings(uiState.selectedLanguage)
    val scope = rememberCoroutineScope()

    var lastExportMessage by remember { mutableStateOf<String?>(null) }
    var isExporting by remember { mutableStateOf(false) }
    var exportSuccess by remember { mutableStateOf<Boolean?>(null) }

    // Launcher for user-selected destination for SQLite DB file
    val createSqliteDocLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                isExporting = true
                val success = exportDatabaseToUri(context, uri)
                isExporting = false
                exportSuccess = success
                if (success) {
                    lastExportMessage = "Database backup saved successfully to chosen location!"
                    Toast.makeText(context, "Backup saved to chosen folder!", Toast.LENGTH_LONG).show()
                } else {
                    lastExportMessage = "Failed to write database to chosen destination."
                    Toast.makeText(context, "Export failed. Please check permissions.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Launcher for user-selected destination for JSON export
    val createJsonDocLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                isExporting = true
                val success = exportJsonToUri(context, uri, uiState)
                isExporting = false
                exportSuccess = success
                if (success) {
                    lastExportMessage = "JSON data backup saved successfully to chosen location!"
                    Toast.makeText(context, "JSON backup saved!", Toast.LENGTH_LONG).show()
                } else {
                    lastExportMessage = "Failed to write JSON backup file."
                }
            }
        }
    }

    val totalItems = uiState.inventoryItems.size
    val totalCategories = uiState.categories.size
    val totalLocations = uiState.locations.size
    val totalShopping = uiState.shoppingItems.size
    val totalAlerts = uiState.restockAlerts.size

    val timestamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("backup_restore_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Summary Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Backup,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Summary",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Database & Local Data Management",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Database Metrics Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Total Items", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("$totalItems", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Categories", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("$totalCategories", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Locations", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("$totalLocations", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                            }
                        }
                    }
                }
            }
        }

        // Feedback Banner
        if (lastExportMessage != null) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (exportSuccess == true) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            if (exportSuccess == true) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = if (exportSuccess == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = lastExportMessage ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = if (exportSuccess == true) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { lastExportMessage = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Dismiss", modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Section: Export Options
        item {
            Text(
                text = "Backup & Export",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Card: Select Destination Folder & Export SQLite
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            Icons.Default.FolderOpen,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Export SQLite Database (.sqlite)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Choose target folder destination via system file picker.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                createSqliteDocLauncher.launch("pantry_backup_${timestamp}.sqlite")
                            },
                            enabled = !isExporting,
                            modifier = Modifier.testTag("btn_export_sqlite_custom_dest")
                        ) {
                            Icon(Icons.Default.SaveAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Select Folder & Save")
                        }
                    }
                }
            }
        }

        // Card: Quick Export to Downloads Folder
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            Icons.Default.Download,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Quick Export to Downloads",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Saves directly to Internal storage → Download folder for easy USB access on PC.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    isExporting = true
                                    val filePath = exportDatabaseToDownloads(context)
                                    isExporting = false
                                    if (filePath != null) {
                                        exportSuccess = true
                                        lastExportMessage = "Saved to Downloads: $filePath"
                                        Toast.makeText(context, "Saved to Downloads: $filePath", Toast.LENGTH_LONG).show()
                                    } else {
                                        exportSuccess = false
                                        lastExportMessage = "Could not write to Downloads directory."
                                    }
                                }
                            },
                            enabled = !isExporting,
                            modifier = Modifier.testTag("btn_quick_export_downloads")
                        ) {
                            Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Export to Downloads")
                        }
                    }
                }
            }
        }

        // Card: Export Readable JSON Backup
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            Icons.Default.Code,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(24.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Export JSON Data Backup",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Human-readable JSON containing all pantry items, categories, locations, and shopping lists.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = {
                                createJsonDocLauncher.launch("pantry_data_${timestamp}.json")
                            },
                            enabled = !isExporting,
                            modifier = Modifier.testTag("btn_export_json_custom_dest")
                        ) {
                            Icon(Icons.Default.SaveAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save JSON")
                        }
                    }
                }
            }
        }
    }
}

private suspend fun exportDatabaseToUri(context: Context, uri: Uri): Boolean = withContext(Dispatchers.IO) {
    try {
        val dbFile = context.getDatabasePath("pantry_inventory_db")
        if (!dbFile.exists()) return@withContext false

        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            FileInputStream(dbFile).use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

private suspend fun exportDatabaseToDownloads(context: Context): String? = withContext(Dispatchers.IO) {
    try {
        val dbFile = context.getDatabasePath("pantry_inventory_db")
        if (!dbFile.exists()) return@withContext null

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs()
        }

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
        val targetFile = File(downloadsDir, "pantry_inventory_db_${timestamp}.sqlite")

        FileInputStream(dbFile).use { input ->
            FileOutputStream(targetFile).use { output ->
                input.copyTo(output)
            }
        }
        targetFile.name
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private suspend fun exportJsonToUri(context: Context, uri: Uri, uiState: PantryUiState): Boolean = withContext(Dispatchers.IO) {
    try {
        val root = JSONObject()
        root.put("app", "Pantry Inventory")
        root.put("version", "1.0.1")
        root.put("exportedAt", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))

        // Inventory Items
        val invArray = JSONArray()
        uiState.inventoryItems.forEach { item ->
            val days = ((item.expirationDateEpoch - System.currentTimeMillis()) / 86400000L).toInt().coerceAtLeast(0)
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("name", item.name)
            obj.put("category", item.category)
            obj.put("location", item.location)
            obj.put("quantity", item.quantity)
            obj.put("unit", item.unit)
            obj.put("minThreshold", item.minThreshold)
            obj.put("daysRemaining", days)
            obj.put("expirationDateEpoch", item.expirationDateEpoch)
            invArray.put(obj)
        }
        root.put("inventoryItems", invArray)

        // Categories
        val catArray = JSONArray()
        uiState.categories.forEach { cat ->
            val obj = JSONObject()
            obj.put("id", cat.id)
            obj.put("name", cat.name)
            obj.put("iconEmoji", cat.iconEmoji)
            obj.put("colorHex", cat.colorHex)
            obj.put("defaultLocation", cat.defaultLocation)
            catArray.put(obj)
        }
        root.put("categories", catArray)

        // Locations
        val locArray = JSONArray()
        uiState.locations.forEach { loc ->
            val obj = JSONObject()
            obj.put("id", loc.id)
            obj.put("name", loc.name)
            obj.put("iconEmoji", loc.iconEmoji)
            obj.put("temperatureZone", loc.temperatureZone)
            obj.put("colorHex", loc.colorHex)
            locArray.put(obj)
        }
        root.put("locations", locArray)

        // Shopping List
        val shopArray = JSONArray()
        uiState.shoppingItems.forEach { shop ->
            val obj = JSONObject()
            obj.put("id", shop.id)
            obj.put("name", shop.name)
            obj.put("quantity", shop.quantity)
            obj.put("unit", shop.unit)
            obj.put("isPurchased", shop.isPurchased)
            obj.put("addedByFamilyMember", shop.addedByFamilyMember)
            shopArray.put(obj)
        }
        root.put("shoppingItems", shopArray)

        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(root.toString(2).toByteArray(Charsets.UTF_8))
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
