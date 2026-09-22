package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import com.example.data.local.entity.LocationEntity
import com.example.ui.i18n.LocalizationManager
import com.example.ui.theme.*
import com.example.ui.viewmodel.PantryUiState
import com.example.ui.viewmodel.PantryViewModel

private val PRESET_LOCATION_EMOJIS = listOf(
    "❄️", "🚪", "🧊", "🧂", "🍞", "🍷", "🧺", "🗄️", "📦", "🧴", "🥘", "🏠", "🌿", "☕", "🍎", "🥫", "🍽️", "🪴", "🗃️", "📍", "🏷️"
)

private val PRESET_LOCATION_COLORS = listOf(
    "#3B82F6", // Blue (Fridge)
    "#F59E0B", // Amber (Pantry)
    "#06B6D4", // Cyan (Freezer)
    "#10B981", // Emerald (Spice Rack)
    "#D97706", // Orange (Countertop)
    "#8B5CF6", // Purple (Wine & Cellar)
    "#EC4899", // Pink
    "#EF4444", // Red
    "#14B8A6", // Teal
    "#64748B"  // Slate
)

private val PRESET_TEMP_ZONES = listOf(
    "Refrigerated (37°F / 3°C)",
    "Deep Freeze (0°F / -18°C)",
    "Room Temp (68°F / 20°C)",
    "Cool & Dry",
    "Ambient / Countertop",
    "Cellar (55°F / 13°C)"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationManagementScreen(
    uiState: PantryUiState,
    viewModel: PantryViewModel,
    modifier: Modifier = Modifier
) {
    val strings = LocalizationManager.getStrings(uiState.selectedLanguage)
    var locationSearchQuery by remember { mutableStateOf("") }
    var locationToDelete by remember { mutableStateOf<LocationEntity?>(null) }
    var selectedZoneFilter by remember { mutableStateOf<String?>(null) }

    val filteredLocations = remember(uiState.locations, locationSearchQuery, selectedZoneFilter) {
        uiState.locations.filter { loc ->
            val matchesQuery = locationSearchQuery.isBlank() ||
                loc.name.contains(locationSearchQuery, ignoreCase = true) ||
                loc.description.contains(locationSearchQuery, ignoreCase = true) ||
                loc.temperatureZone.contains(locationSearchQuery, ignoreCase = true)
            val matchesZone = selectedZoneFilter == null || loc.temperatureZone.contains(selectedZoneFilter.orEmpty(), ignoreCase = true)
            matchesQuery && matchesZone
        }
    }

    val totalLocations = uiState.locations.size
    val totalInventoryItems = uiState.inventoryItems.size

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddLocationDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("fab_add_location")
            ) {
                Icon(Icons.Default.Add, contentDescription = strings.addLocation)
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
                                        Icons.Default.Place,
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
                                    text = strings.navLocations,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Stats Summary Row with Title Above Sub-Blocks
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
                                        text = strings.totalLocations,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "$totalLocations",
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
                                        text = strings.itemsTracked,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "$totalInventoryItems",
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
                    value = locationSearchQuery,
                    onValueChange = { locationSearchQuery = it },
                    placeholder = { Text(strings.searchLocationsPlaceholder, style = MaterialTheme.typography.bodyMedium) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (locationSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { locationSearchQuery = "" }) {
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
                        .testTag("search_location_input")
                )
            }

            // Temperature / Storage Zone Quick Filters
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        val allLabel = when (uiState.selectedLanguage.lowercase()) {
                            "de" -> "Alle Zonen"
                            "it" -> "Tutte le Zone"
                            "pt" -> "Todas as Zonas"
                            else -> "All Zones"
                        }
                        FilterChip(
                            selected = selectedZoneFilter == null,
                            onClick = { selectedZoneFilter = null },
                            label = { Text("$allLabel (${uiState.locations.size})", style = MaterialTheme.typography.labelSmall) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("filter_zone_all")
                        )
                    }
                    val zoneNames = if (uiState.temperatureZones.isNotEmpty()) {
                        uiState.temperatureZones.map { it.name }
                    } else {
                        listOf("Refrigerated", "Freeze", "Room Temp", "Cool & Dry", "Cellar")
                    }
                    items(zoneNames) { zone ->
                        val count = uiState.locations.count { it.temperatureZone.contains(zone, ignoreCase = true) }
                        FilterChip(
                            selected = selectedZoneFilter == zone,
                            onClick = { selectedZoneFilter = if (selectedZoneFilter == zone) null else zone },
                            label = { Text("$zone ($count)", style = MaterialTheme.typography.labelSmall) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("filter_zone_$zone")
                        )
                    }
                }
            }

            // Locations List
            if (filteredLocations.isEmpty()) {
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
                                text = "🏠",
                                fontSize = 42.sp
                            )
                            Text(
                                text = if (locationSearchQuery.isNotBlank()) "No locations matching \"$locationSearchQuery\"" else "No storage locations found",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Create storage locations (e.g. Fridge, Pantry, Freezer, Spice Rack) to organize your household supplies.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = { viewModel.openAddLocationDialog() },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(strings.addLocation)
                            }
                        }
                    }
                }
            } else {
                items(filteredLocations, key = { it.id }) { location ->
                    val inventoryCount = uiState.inventoryItems.count {
                        it.location.equals(location.name, ignoreCase = true)
                    }

                    LocationCard(
                        location = location,
                        inventoryCount = inventoryCount,
                        onEdit = { viewModel.openEditLocationDialog(location) },
                        onDelete = { locationToDelete = location },
                        onViewItems = {
                            viewModel.setLocationFilter(location.name)
                            viewModel.selectTab(0) // Switch to Inventory screen
                        },
                        modifier = Modifier.testTag("location_card_${location.id}")
                    )
                }
            }
        }
    }

    // Location Create / Edit Dialog
    if (uiState.showLocationDialog) {
        val zoneOptions = if (uiState.temperatureZones.isNotEmpty()) {
            uiState.temperatureZones.map { "${it.name} (${it.targetRange})" }
        } else {
            PRESET_TEMP_ZONES
        }

        LocationCrudDialog(
            locationToEdit = uiState.locationToEdit,
            languageCode = uiState.selectedLanguage,
            availableTempZones = zoneOptions,
            onDismiss = { viewModel.dismissLocationDialog() },
            onSave = { name, emoji, desc, colorHex, tempZone, id ->
                viewModel.saveLocation(
                    name = name,
                    iconEmoji = emoji,
                    description = desc,
                    colorHex = colorHex,
                    temperatureZone = tempZone,
                    id = id
                )
            }
        )
    }

    // Delete Location Confirmation Dialog
    locationToDelete?.let { location ->
        val itemsInLocation = uiState.inventoryItems.count { it.location.equals(location.name, ignoreCase = true) }
        AlertDialog(
            onDismissRequest = { locationToDelete = null },
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
                    text = strings.confirmDeleteLocation,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Are you sure you want to delete storage location \"${location.name}\"?"
                    )
                    if (itemsInLocation > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⚠️ $itemsInLocation item(s) are currently stored in this location. ${strings.deleteLocationWarning}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    } else {
                        Text(
                            text = strings.deleteLocationWarning,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteLocation(location)
                        locationToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("btn_confirm_delete_location")
                ) {
                    Text(strings.delete)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { locationToDelete = null },
                    modifier = Modifier.testTag("btn_cancel_delete_location")
                ) {
                    Text(strings.cancel)
                }
            }
        )
    }
}

@Composable
fun LocationCard(
    location: LocationEntity,
    inventoryCount: Int,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onViewItems: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = remember(location.colorHex) {
        try {
            Color(android.graphics.Color.parseColor(location.colorHex))
        } catch (e: Exception) {
            Color(0xFF10B981)
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Row: Emoji, Name, Temperature Zone, Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Emoji Avatar with accent color border
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = accentColor.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(2.dp, accentColor.copy(alpha = 0.4f)),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = location.iconEmoji.ifBlank { "🏠" },
                                fontSize = 24.sp
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = location.name,
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
                                    text = location.temperatureZone,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                // Edit and Delete buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_edit_location_${location.id}")
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Location",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_delete_location_${location.id}")
                    ) {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = "Delete Location",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Description if present
            if (location.description.isNotBlank()) {
                Text(
                    text = location.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            // Footer stats and quick action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📦 $inventoryCount items housed",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                TextButton(
                    onClick = onViewItems,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("btn_view_items_location_${location.id}")
                ) {
                    Text("View Items", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationCrudDialog(
    locationToEdit: LocationEntity?,
    languageCode: String = "en",
    availableTempZones: List<String> = PRESET_TEMP_ZONES,
    onDismiss: () -> Unit,
    onSave: (name: String, emoji: String, desc: String, colorHex: String, tempZone: String, id: Long) -> Unit
) {
    val strings = LocalizationManager.getStrings(languageCode)
    var name by remember { mutableStateOf(locationToEdit?.name ?: "") }
    var selectedEmoji by remember { mutableStateOf(locationToEdit?.iconEmoji ?: "❄️") }
    var description by remember { mutableStateOf(locationToEdit?.description ?: "") }
    var selectedColor by remember { mutableStateOf(locationToEdit?.colorHex ?: "#3B82F6") }
    val defaultTempZone = availableTempZones.firstOrNull() ?: "Refrigerated (37°F / 3°C)"
    var selectedTempZone by remember { mutableStateOf(locationToEdit?.temperatureZone ?: defaultTempZone) }
    var nameError by remember { mutableStateOf(false) }

    val isEditing = locationToEdit != null
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
                    if (isEditing) Icons.Default.Edit else Icons.Default.AddLocationAlt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (isEditing) strings.editLocation else strings.addLocation,
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
                                Text(text = selectedEmoji.ifBlank { "🏠" }, fontSize = 20.sp)
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = name.ifBlank { strings.locationName },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (name.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$selectedTempZone • ${description.ifBlank { strings.locationDescription }}",
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
                    label = { Text("${strings.locationName} *") },
                    placeholder = { Text(strings.locationName) },
                    isError = nameError,
                    supportingText = {
                        if (nameError) Text("${strings.locationName} is required", color = MaterialTheme.colorScheme.error)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_location_name")
                )

                // Emoji Picker
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "${strings.locationIcon}: $selectedEmoji",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(PRESET_LOCATION_EMOJIS) { emoji ->
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

                // Temperature / Storage Zone Selector
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = strings.locationTemperatureZone,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(availableTempZones) { zone ->
                            val isSelected = selectedTempZone == zone
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedTempZone = zone },
                                label = { Text(zone, style = MaterialTheme.typography.labelSmall) },
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }
                }

                // Color Picker
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = strings.locationColor,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(PRESET_LOCATION_COLORS) { hexColor ->
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
                    label = { Text(strings.locationDescription) },
                    placeholder = { Text(strings.locationDescription) },
                    maxLines = 2,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_location_description")
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
                            selectedTempZone,
                            locationToEdit?.id ?: 0L
                        )
                    }
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("btn_save_location")
            ) {
                Text(if (isEditing) strings.save else strings.create)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("btn_cancel_location")
            ) {
                Text(strings.cancel)
            }
        }
    )
}
