package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.TemperatureZoneEntity
import com.example.ui.i18n.LocalizationManager
import com.example.ui.theme.*
import com.example.ui.viewmodel.PantryUiState
import com.example.ui.viewmodel.PantryViewModel

private val PRESET_ZONE_EMOJIS = listOf(
    "🧊", "❄️", "🍷", "🚪", "🍞", "🌡️", "🍲", "🍶", "🥫", "🧺", "📦", "🧁"
)

private val PRESET_ZONE_COLORS = listOf(
    "#06B6D4", // Cyan (Deep Freeze)
    "#3B82F6", // Blue (Refrigerated)
    "#8B5CF6", // Purple (Cellar)
    "#F59E0B", // Amber (Room Temp)
    "#D97706", // Orange (Warm)
    "#10B981", // Emerald (Cool & Dry)
    "#EC4899", // Pink
    "#64748B"  // Slate
)

private val PRESET_TARGET_TEMPS = listOf(
    "0°F / -18°C",
    "37°F / 3°C",
    "55°F / 13°C",
    "68°F / 20°C",
    "72°F / 22°C",
    "Ambient",
    "Cool & Dry (15°C)"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureZoneManagementScreen(
    uiState: PantryUiState,
    viewModel: PantryViewModel,
    modifier: Modifier = Modifier
) {
    val strings = LocalizationManager.getStrings(uiState.selectedLanguage)
    var zoneSearchQuery by remember { mutableStateOf("") }
    var zoneToDelete by remember { mutableStateOf<TemperatureZoneEntity?>(null) }

    val filteredZones = remember(uiState.temperatureZones, zoneSearchQuery) {
        uiState.temperatureZones.filter { zone ->
            if (zoneSearchQuery.isBlank()) true
            else {
                zone.name.contains(zoneSearchQuery, ignoreCase = true) ||
                zone.description.contains(zoneSearchQuery, ignoreCase = true) ||
                zone.targetRange.contains(zoneSearchQuery, ignoreCase = true)
            }
        }
    }

    val totalZones = uiState.temperatureZones.size

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddTemperatureZoneDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("fab_add_temperature_zone")
            ) {
                Icon(Icons.Default.Add, contentDescription = strings.addTempZone)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Hero Card: Summary Block
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.DeviceThermostat,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = strings.summary,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = strings.navTempZones,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Stats Summary Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = strings.totalTempZones,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "$totalZones",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = strings.totalLocations,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${uiState.locations.size}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Compact Search Bar
            item {
                OutlinedTextField(
                    value = zoneSearchQuery,
                    onValueChange = { zoneSearchQuery = it },
                    placeholder = { Text(strings.searchTempZonesPlaceholder, style = MaterialTheme.typography.bodyMedium) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (zoneSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { zoneSearchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search", modifier = Modifier.size(18.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_temperature_zone_input")
                )
            }

            // List of Zones
            if (filteredZones.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "🌡️",
                                fontSize = 42.sp
                            )
                            Text(
                                text = strings.noItemsFound,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Button(
                                onClick = { viewModel.openAddTemperatureZoneDialog() },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("btn_empty_add_temp_zone")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(strings.addTempZone)
                            }
                        }
                    }
                }
            } else {
                items(filteredZones, key = { it.id }) { zone ->
                    val matchingLocationsCount = uiState.locations.count {
                        it.temperatureZone.contains(zone.name, ignoreCase = true)
                    }

                    TemperatureZoneCard(
                        zone = zone,
                        locationCount = matchingLocationsCount,
                        onEdit = { viewModel.openEditTemperatureZoneDialog(zone) },
                        onDelete = { zoneToDelete = zone },
                        modifier = Modifier.testTag("temperature_zone_card_${zone.id}")
                    )
                }
            }
        }
    }

    // Zone Create / Edit Dialog
    if (uiState.showTemperatureZoneDialog) {
        TemperatureZoneCrudDialog(
            zoneToEdit = uiState.temperatureZoneToEdit,
            languageCode = uiState.selectedLanguage,
            onDismiss = { viewModel.dismissTemperatureZoneDialog() },
            onSave = { name, emoji, desc, colorHex, targetTemp, id ->
                viewModel.saveTemperatureZone(
                    name = name,
                    iconEmoji = emoji,
                    description = desc,
                    colorHex = colorHex,
                    targetTemperature = targetTemp,
                    id = id
                )
            }
        )
    }

    // Delete Confirmation Dialog
    zoneToDelete?.let { zone ->
        AlertDialog(
            onDismissRequest = { zoneToDelete = null },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = strings.confirmDeleteTempZone,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${zone.iconEmoji} ${zone.name}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = strings.deleteTempZoneWarning,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteTemperatureZone(zone)
                        zoneToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_confirm_delete_temp_zone")
                ) {
                    Text(strings.delete, color = MaterialTheme.colorScheme.onError)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { zoneToDelete = null },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_cancel_delete_temp_zone")
                ) {
                    Text(strings.cancel)
                }
            }
        )
    }
}

@Composable
fun TemperatureZoneCard(
    zone: TemperatureZoneEntity,
    locationCount: Int,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val zoneColor = remember(zone.colorHex) {
        try {
            Color(android.graphics.Color.parseColor(zone.colorHex))
        } catch (e: Exception) {
            Color(0xFF3B82F6)
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = zoneColor.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, zoneColor),
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = zone.iconEmoji.ifBlank { "🌡️" },
                                fontSize = 22.sp
                            )
                        }
                    }

                    Column {
                        Text(
                            text = zone.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = zone.targetRange,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                // Action buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_edit_temp_zone_${zone.id}")
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Zone",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_delete_temp_zone_${zone.id}")
                    ) {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = "Delete Zone",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            if (zone.description.isNotBlank()) {
                Text(
                    text = zone.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🏠 $locationCount locations assigned",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureZoneCrudDialog(
    zoneToEdit: TemperatureZoneEntity?,
    languageCode: String = "en",
    onDismiss: () -> Unit,
    onSave: (name: String, emoji: String, desc: String, colorHex: String, targetTemp: String, id: Long) -> Unit
) {
    val strings = LocalizationManager.getStrings(languageCode)
    var name by remember { mutableStateOf(zoneToEdit?.name ?: "") }
    var selectedEmoji by remember { mutableStateOf(zoneToEdit?.iconEmoji ?: "🌡️") }
    var description by remember { mutableStateOf(zoneToEdit?.description ?: "") }
    var selectedColor by remember { mutableStateOf(zoneToEdit?.colorHex ?: "#3B82F6") }
    var targetTemperature by remember { mutableStateOf(zoneToEdit?.targetRange ?: "37°F / 3°C") }
    var nameError by remember { mutableStateOf(false) }

    val isEditing = zoneToEdit != null
    val parsedColor = remember(selectedColor) {
        try {
            Color(android.graphics.Color.parseColor(selectedColor))
        } catch (e: Exception) {
            Color(0xFF3B82F6)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    if (isEditing) Icons.Default.Edit else Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (isEditing) strings.editTempZone else strings.addTempZone,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Live Preview Card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = parsedColor.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, parsedColor),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = selectedEmoji.ifBlank { "🌡️" }, fontSize = 20.sp)
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = name.ifBlank { "Zone Name" },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (name.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$targetTemperature • ${description.ifBlank { "No description set" }}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Name Input
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (nameError && it.isNotBlank()) nameError = false
                    },
                    label = { Text(strings.tempZoneName) },
                    placeholder = { Text("e.g. Deep Freeze, Cellar, Warm Pantry") },
                    isError = nameError,
                    supportingText = {
                        if (nameError) Text("Zone name is required", color = MaterialTheme.colorScheme.error)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_temp_zone_name")
                )

                // Emoji Picker
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = strings.tempZoneIcon,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(PRESET_ZONE_EMOJIS) { emoji ->
                            val isSelected = selectedEmoji == emoji
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clickable { selectedEmoji = emoji }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = emoji, fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }

                // Target Temperature Range Selector / Input
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = strings.tempZoneRange,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(PRESET_TARGET_TEMPS) { temp ->
                            val isSelected = targetTemperature == temp
                            FilterChip(
                                selected = isSelected,
                                onClick = { targetTemperature = temp },
                                label = { Text(temp, style = MaterialTheme.typography.labelSmall) },
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                    OutlinedTextField(
                        value = targetTemperature,
                        onValueChange = { targetTemperature = it },
                        placeholder = { Text("Custom temperature range") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_temp_zone_range")
                    )
                }

                // Color Picker
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = strings.tempZoneColor,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(PRESET_ZONE_COLORS) { hexColor ->
                            val isSelected = selectedColor.equals(hexColor, ignoreCase = true)
                            val col = try {
                                Color(android.graphics.Color.parseColor(hexColor))
                            } catch (e: Exception) {
                                Color.Gray
                            }
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(col)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColor = hexColor },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Description Input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(strings.tempZoneDescription) },
                    placeholder = { Text("e.g. Sub-zero preservation for meats and prepared meals") },
                    maxLines = 2,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_temp_zone_description")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        nameError = true
                    } else {
                        onSave(
                            name.trim(),
                            selectedEmoji,
                            description.trim(),
                            selectedColor,
                            targetTemperature.trim(),
                            zoneToEdit?.id ?: 0L
                        )
                    }
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("btn_save_temp_zone")
            ) {
                Text(if (isEditing) strings.save else strings.create)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("btn_cancel_temp_zone")
            ) {
                Text(strings.cancel)
            }
        }
    )
}
