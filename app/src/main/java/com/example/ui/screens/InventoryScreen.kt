package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.InventoryItemEntity
import com.example.ui.components.OracleCloudStatusBadge
import com.example.ui.i18n.LocalizationManager
import com.example.ui.theme.*
import com.example.ui.viewmodel.PantryUiState
import com.example.ui.viewmodel.PantryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    uiState: PantryUiState,
    viewModel: PantryViewModel,
    modifier: Modifier = Modifier
) {
    val strings = LocalizationManager.getStrings(uiState.selectedLanguage)
    val dynamicCategories = remember(uiState.categories) {
        val names = if (uiState.categories.isNotEmpty()) {
            uiState.categories.map { it.name }
        } else {
            listOf("Produce", "Dairy & Eggs", "Pantry", "Meat & Seafood", "Beverages", "Snacks")
        }
        listOf("All") + names
    }
    val dynamicLocations = remember(uiState.locations) {
        val names = if (uiState.locations.isNotEmpty()) {
            uiState.locations.map { it.name }
        } else {
            listOf("Fridge", "Pantry", "Freezer", "Spice Rack", "Countertop", "Wine & Cellar")
        }
        listOf("All") + names
    }

    val allLabel = when (uiState.selectedLanguage.lowercase()) {
        "de" -> "Alle"
        "it" -> "Tutti"
        "pt" -> "Todos"
        else -> "All"
    }

    val filteredItems = remember(uiState.inventoryItems, uiState.selectedCategory, uiState.selectedLocation, uiState.searchQuery, uiState.selectedLanguage) {
        uiState.inventoryItems.filter { item ->
            val matchCat = uiState.selectedCategory == "All" || item.category.equals(uiState.selectedCategory, ignoreCase = true)
            val matchLoc = uiState.selectedLocation == "All" || item.location.equals(uiState.selectedLocation, ignoreCase = true)
            val localizedName = LocalizationManager.localizeItemName(item.name, uiState.selectedLanguage)
            val localizedCat = LocalizationManager.localizeCategory(item.category, uiState.selectedLanguage)
            val matchSearch = uiState.searchQuery.isEmpty() ||
                    item.name.contains(uiState.searchQuery, ignoreCase = true) ||
                    localizedName.contains(uiState.searchQuery, ignoreCase = true) ||
                    item.category.contains(uiState.searchQuery, ignoreCase = true) ||
                    localizedCat.contains(uiState.searchQuery, ignoreCase = true)
            matchCat && matchLoc && matchSearch
        }
    }

    Scaffold(
        modifier = modifier.testTag("inventory_screen"),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.setShowAddItemDialog(true) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("fab_add_inventory_item")
            ) {
                Icon(Icons.Default.Add, contentDescription = strings.addItemTitle)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Hero Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hero_pantry),
                        contentDescription = "Pantry Hero Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.75f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // [HIDED_WAITING_NEW_VERSIONS]
                        // Row(
                        //     modifier = Modifier.fillMaxWidth(),
                        //     horizontalArrangement = Arrangement.SpaceBetween,
                        //     verticalAlignment = Alignment.CenterVertically
                        // ) {
                        //     // [HIDED_WAITING_NEW_VERSIONS] Oracle 26ai Text
                        //     OracleCloudStatusBadge(
                        //         connectionState = uiState.connectionState,
                        //         isOfflineMode = uiState.isOfflineMode,
                        //         onClick = { viewModel.selectTab(3) }
                        //     )
                        //     // [HIDED_WAITING_NEW_VERSIONS] E2EE Active Text
                        //     Surface(
                        //         color = Color.Black.copy(alpha = 0.4f),
                        //         shape = RoundedCornerShape(12.dp)
                        //     ) {
                        //         Row(
                        //             modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        //             verticalAlignment = Alignment.CenterVertically,
                        //             horizontalArrangement = Arrangement.spacedBy(4.dp)
                        //         ) {
                        //             Icon(
                        //                 Icons.Default.Lock,
                        //                 contentDescription = "E2EE Secured",
                        //                 tint = EmeraldPrimaryDark,
                        //                 modifier = Modifier.size(12.dp)
                        //             )
                        //             Text(
                        //                 "E2EE Active",
                        //                 color = Color.White,
                        //                 style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp)
                        //             )
                        //         }
                        //     }
                        // }
                        Spacer(modifier = Modifier.weight(1f))
                        Column {
                            Text(
                                text = strings.inventoryHeroTitle,
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${uiState.inventoryItems.size} ${strings.itemsTracked} • ${uiState.restockAlerts.size} ${strings.alertsCount}",
                                color = Color.White.copy(alpha = 0.85f),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            // Compact Search Bar
            item {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .testTag("inventory_search_field"),
                    placeholder = { Text(strings.searchPlaceholder, style = MaterialTheme.typography.bodyMedium) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(20.dp)) },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search", modifier = Modifier.size(18.dp))
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    singleLine = true
                )
            }

            // Category Chips Horizontal Menu
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(dynamicCategories) { category ->
                        val emoji = uiState.categories.find { it.name.equals(category, ignoreCase = true) }?.iconEmoji ?: ""
                        val displayCat = if (category == "All") allLabel else LocalizationManager.localizeCategory(category, uiState.selectedLanguage)
                        val fullLabel = if (emoji.isNotBlank()) "$emoji $displayCat" else displayCat

                        FilterChip(
                            selected = uiState.selectedCategory == category,
                            onClick = { viewModel.setCategoryFilter(category) },
                            label = { Text(fullLabel, style = MaterialTheme.typography.labelSmall) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("filter_chip_$category"),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }

                    // Add / Manage Category chip at end of horizontal menu
                    item {
                        SuggestionChip(
                            onClick = { viewModel.selectTab(1) },
                            label = { Text("+ ${strings.addCategory}", style = MaterialTheme.typography.labelSmall) },
                            icon = { Icon(Icons.Default.Category, contentDescription = null, modifier = Modifier.size(14.dp)) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                labelColor = MaterialTheme.colorScheme.primary,
                                iconContentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = SuggestionChipDefaults.suggestionChipBorder(enabled = true, borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                            modifier = Modifier.testTag("chip_manage_categories")
                        )
                    }
                }
            }

            // Location Filters Horizontal Menu (without Location: label)
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(dynamicLocations) { loc ->
                        val isSelected = uiState.selectedLocation == loc
                        val emoji = uiState.locations.find { it.name.equals(loc, ignoreCase = true) }?.iconEmoji ?: ""
                        val displayLoc = if (loc == "All") allLabel else LocalizationManager.localizeLocation(loc, uiState.selectedLanguage)
                        val fullLocLabel = if (emoji.isNotBlank()) "$emoji $displayLoc" else displayLoc

                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setLocationFilter(loc) },
                            label = { Text(fullLocLabel, style = MaterialTheme.typography.labelSmall) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("filter_chip_location_$loc"),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        )
                    }

                    // Add / Manage Location chip at end of horizontal menu
                    item {
                        SuggestionChip(
                            onClick = { viewModel.selectTab(2) },
                            label = { Text("+ ${strings.addLocation}", style = MaterialTheme.typography.labelSmall) },
                            icon = { Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(14.dp)) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                labelColor = MaterialTheme.colorScheme.secondary,
                                iconContentColor = MaterialTheme.colorScheme.secondary
                            ),
                            border = SuggestionChipDefaults.suggestionChipBorder(enabled = true, borderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)),
                            modifier = Modifier.testTag("chip_manage_locations")
                        )
                    }
                }
            }

            // Inventory Items List
            if (filteredItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Kitchen,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Text(
                                strings.noItemsFound,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                strings.tapToAdd,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            } else {
                items(filteredItems, key = { it.id }) { item ->
                    InventoryItemCard(
                        item = item,
                        languageCode = uiState.selectedLanguage,
                        onQuantityChange = { delta -> viewModel.adjustQuantity(item, delta) },
                        onEdit = { viewModel.openEditInventoryItemDialog(item) },
                        onInspectCiphertext = { viewModel.inspectItemCiphertext(item) },
                        onDelete = { viewModel.deleteInventoryItem(item) }
                    )
                }
            }
        }
    }

    // Add Item Dialog
    if (uiState.showAddItemDialog) {
        AddInventoryItemDialog(
            languageCode = uiState.selectedLanguage,
            availableCategories = uiState.categories,
            availableLocations = uiState.locations,
            onDismiss = { viewModel.setShowAddItemDialog(false) },
            onConfirm = { name, cat, loc, qty, unit, threshold, days ->
                viewModel.addInventoryItem(name, cat, loc, qty, unit, threshold, days)
            }
        )
    }

    // Edit Item Dialog
    if (uiState.showEditItemDialog && uiState.itemToEdit != null) {
        EditInventoryItemDialog(
            item = uiState.itemToEdit,
            languageCode = uiState.selectedLanguage,
            availableCategories = uiState.categories,
            availableLocations = uiState.locations,
            onDismiss = { viewModel.dismissEditInventoryItemDialog() },
            onConfirm = { id, name, cat, loc, qty, unit, threshold, days ->
                viewModel.updateInventoryItem(id, name, cat, loc, qty, unit, threshold, days)
            }
        )
    }
}

@Composable
fun InventoryItemCard(
    item: InventoryItemEntity,
    languageCode: String,
    onQuantityChange: (Double) -> Unit,
    onEdit: () -> Unit,
    onInspectCiphertext: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalizationManager.getStrings(languageCode)
    val displayName = LocalizationManager.localizeItemName(item.name, languageCode)
    val displayCategory = LocalizationManager.localizeCategory(item.category, languageCode)
    val displayLocation = LocalizationManager.localizeLocation(item.location, languageCode)
    val isLowStock = item.quantity <= item.minThreshold
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("inventory_item_${item.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = displayCategory,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = displayLocation,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                // Options Menu
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Options")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(strings.editCategory.replace("Category", "Item").replace("Categoria", "Item")) },
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(strings.inspectE2ee) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            onClick = {
                                showMenu = false
                                onInspectCiphertext()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(strings.delete, color = StatusCritical) },
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = StatusCritical) },
                            onClick = {
                                showMenu = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            // Quantity Stepper & Stock Gauge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${item.quantity}",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isLowStock) StatusCritical else MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = item.unit,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                    Text(
                        text = "${strings.threshold} ${item.minThreshold} ${item.unit}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                // +/- Steppers
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalIconButton(
                        onClick = { onQuantityChange(-1.0) },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease")
                    }
                    FilledTonalIconButton(
                        onClick = { onQuantityChange(1.0) },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase")
                    }
                }
            }

            // Low Stock / Restock Alert Indicator
            if (isLowStock) {
                Surface(
                    color = StatusCritical.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = StatusCritical,
                            modifier = Modifier.size(16.dp)
                        )
                        val alertMsg = if (item.quantity <= 0) {
                            when (languageCode.lowercase()) {
                                "de" -> "Nicht vorrätig! Automatischer Alarm aktiv."
                                "it" -> "Esaurito! Avviso automatico attivo."
                                "pt" -> "Esgotado! Alerta automático ativo."
                                else -> "Out of stock! Automatic alert active."
                            }
                        } else {
                            "${strings.lowStock} (< ${item.minThreshold})"
                        }
                        Text(
                            text = alertMsg,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = StatusCritical
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInventoryItemDialog(
    languageCode: String,
    availableCategories: List<com.example.data.local.entity.CategoryEntity> = emptyList(),
    availableLocations: List<com.example.data.local.entity.LocationEntity> = emptyList(),
    onDismiss: () -> Unit,
    onConfirm: (name: String, cat: String, loc: String, qty: Double, unit: String, threshold: Double, daysExpiry: Int) -> Unit
) {
    val strings = LocalizationManager.getStrings(languageCode)
    val categoryList = if (availableCategories.isNotEmpty()) {
        availableCategories.map { it.name }
    } else {
        listOf("Produce", "Dairy & Eggs", "Pantry", "Meat & Seafood", "Beverages", "Snacks", "Bakery")
    }

    val locationList = if (availableLocations.isNotEmpty()) {
        availableLocations.map { it.name }
    } else {
        listOf("Fridge", "Pantry", "Freezer", "Spice Rack", "Countertop", "Wine & Cellar")
    }

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(categoryList.firstOrNull() ?: "Produce") }
    var location by remember { mutableStateOf(locationList.firstOrNull() ?: "Fridge") }
    var quantityStr by remember { mutableStateOf("1.0") }
    var unit by remember { mutableStateOf("units") }
    var thresholdStr by remember { mutableStateOf("1.0") }
    var daysExpiryStr by remember { mutableStateOf("7") }

    val locations = locationList

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.addItemTitle, style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(strings.itemName) },
                    placeholder = { Text("e.g., Almond Milk") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = quantityStr,
                        onValueChange = { quantityStr = it },
                        label = { Text(strings.quantity) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text(strings.unit) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = thresholdStr,
                        onValueChange = { thresholdStr = it },
                        label = { Text(strings.minThreshold) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = daysExpiryStr,
                        onValueChange = { daysExpiryStr = it },
                        label = { Text(strings.daysRemaining) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Text(strings.category, style = MaterialTheme.typography.labelSmall)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categoryList) { cat ->
                        val emoji = availableCategories.find { it.name.equals(cat, true) }?.iconEmoji ?: ""
                        val labelText = if (emoji.isNotBlank()) "$emoji ${LocalizationManager.localizeCategory(cat, languageCode)}" else LocalizationManager.localizeCategory(cat, languageCode)
                        FilterChip(
                            selected = category == cat,
                            onClick = {
                                category = cat
                                val defaultLoc = availableCategories.find { it.name.equals(cat, true) }?.defaultLocation
                                if (!defaultLoc.isNullOrBlank()) {
                                    location = defaultLoc
                                }
                            },
                            label = { Text(labelText, fontSize = 11.sp) }
                        )
                    }
                }

                Text(strings.location, style = MaterialTheme.typography.labelSmall)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(locations) { loc ->
                        FilterChip(
                            selected = location == loc,
                            onClick = { location = loc },
                            label = { Text(LocalizationManager.localizeLocation(loc, languageCode), fontSize = 11.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val qty = quantityStr.toDoubleOrNull() ?: 1.0
                        val threshold = thresholdStr.toDoubleOrNull() ?: 1.0
                        val days = daysExpiryStr.toIntOrNull() ?: 7
                        onConfirm(name, category, location, qty, unit, threshold, days)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text(strings.create)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(strings.cancel)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditInventoryItemDialog(
    item: InventoryItemEntity,
    languageCode: String,
    availableCategories: List<com.example.data.local.entity.CategoryEntity> = emptyList(),
    availableLocations: List<com.example.data.local.entity.LocationEntity> = emptyList(),
    onDismiss: () -> Unit,
    onConfirm: (id: Long, name: String, cat: String, loc: String, qty: Double, unit: String, threshold: Double, daysExpiry: Int) -> Unit
) {
    val strings = LocalizationManager.getStrings(languageCode)
    val categoryList = if (availableCategories.isNotEmpty()) {
        availableCategories.map { it.name }
    } else {
        listOf("Produce", "Dairy & Eggs", "Pantry", "Meat & Seafood", "Beverages", "Snacks", "Bakery")
    }

    val locationList = if (availableLocations.isNotEmpty()) {
        availableLocations.map { it.name }
    } else {
        listOf("Fridge", "Pantry", "Freezer", "Spice Rack", "Countertop", "Wine & Cellar")
    }

    var name by remember { mutableStateOf(item.name) }
    var category by remember { mutableStateOf(item.category) }
    var location by remember { mutableStateOf(item.location) }
    var quantityStr by remember { mutableStateOf(item.quantity.toString()) }
    var unit by remember { mutableStateOf(item.unit) }
    var thresholdStr by remember { mutableStateOf(item.minThreshold.toString()) }
    val initialDays = ((item.expirationDateEpoch - System.currentTimeMillis()) / 86400000L).coerceAtLeast(1)
    var daysExpiryStr by remember { mutableStateOf(initialDays.toString()) }

    val locations = locationList

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.editItemTitle, style = MaterialTheme.typography.titleLarge) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(strings.itemName) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = quantityStr,
                        onValueChange = { quantityStr = it },
                        label = { Text(strings.quantity) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text(strings.unit) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = thresholdStr,
                        onValueChange = { thresholdStr = it },
                        label = { Text(strings.minThreshold) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = daysExpiryStr,
                        onValueChange = { daysExpiryStr = it },
                        label = { Text(strings.daysRemaining) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Text(strings.category, style = MaterialTheme.typography.labelSmall)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categoryList) { cat ->
                        val emoji = availableCategories.find { it.name.equals(cat, true) }?.iconEmoji ?: ""
                        val labelText = if (emoji.isNotBlank()) "$emoji ${LocalizationManager.localizeCategory(cat, languageCode)}" else LocalizationManager.localizeCategory(cat, languageCode)
                        FilterChip(
                            selected = category == cat,
                            onClick = {
                                category = cat
                                val defaultLoc = availableCategories.find { it.name.equals(cat, true) }?.defaultLocation
                                if (!defaultLoc.isNullOrBlank()) {
                                    location = defaultLoc
                                }
                            },
                            label = { Text(labelText, fontSize = 11.sp) }
                        )
                    }
                }

                Text(strings.location, style = MaterialTheme.typography.labelSmall)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(locations) { loc ->
                        FilterChip(
                            selected = location == loc,
                            onClick = { location = loc },
                            label = { Text(LocalizationManager.localizeLocation(loc, languageCode), fontSize = 11.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val qty = quantityStr.toDoubleOrNull() ?: 1.0
                        val threshold = thresholdStr.toDoubleOrNull() ?: 1.0
                        val days = daysExpiryStr.toIntOrNull() ?: 7
                        onConfirm(item.id, name, category, location, qty, unit, threshold, days)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text(strings.save)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(strings.cancel)
            }
        }
    )
}
