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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CategoryEntity
import com.example.ui.i18n.LocalizationManager
import com.example.ui.theme.*
import com.example.ui.viewmodel.PantryUiState
import com.example.ui.viewmodel.PantryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    uiState: PantryUiState,
    viewModel: PantryViewModel,
    modifier: Modifier = Modifier
) {
    val strings = LocalizationManager.getStrings(uiState.selectedLanguage)
    var categorySearchQuery by remember { mutableStateOf("") }
    var selectedLocationFilter by remember { mutableStateOf("All") }
    var categoryToDelete by remember { mutableStateOf<CategoryEntity?>(null) }

    val allLabel = when (uiState.selectedLanguage.lowercase()) {
        "de" -> "Alle"
        "it" -> "Tutti"
        "pt" -> "Todos"
        else -> "All"
    }

    val dynamicLocations = remember(uiState.locations, uiState.categories) {
        val names = if (uiState.locations.isNotEmpty()) {
            uiState.locations.map { it.name }
        } else {
            listOf("Fridge", "Pantry", "Freezer", "Spice Rack", "Countertop", "Wine & Cellar")
        }
        listOf("All") + names
    }

    val filteredCategories = remember(uiState.categories, categorySearchQuery, selectedLocationFilter) {
        uiState.categories.filter { cat ->
            val matchesLoc = selectedLocationFilter == "All" || cat.defaultLocation.equals(selectedLocationFilter, ignoreCase = true)
            val matchesSearch = categorySearchQuery.isBlank() ||
                cat.name.contains(categorySearchQuery, ignoreCase = true) ||
                cat.description.contains(categorySearchQuery, ignoreCase = true) ||
                cat.defaultLocation.contains(categorySearchQuery, ignoreCase = true)
            matchesLoc && matchesSearch
        }
    }

    val totalCategories = uiState.categories.size
    val totalInventoryItems = uiState.inventoryItems.size

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddCategoryDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("fab_add_category")
            ) {
                Icon(Icons.Default.Add, contentDescription = strings.addCategory)
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
                                        Icons.Default.Category,
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
                                    text = strings.navCategories,
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
                                        text = strings.totalCategories,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "$totalCategories",
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

            // Compact Search Bar for Categories
            item {
                OutlinedTextField(
                    value = categorySearchQuery,
                    onValueChange = { categorySearchQuery = it },
                    placeholder = { Text(strings.searchCategoriesPlaceholder, style = MaterialTheme.typography.bodyMedium) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search Categories",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (categorySearchQuery.isNotEmpty()) {
                            IconButton(onClick = { categorySearchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear Search", modifier = Modifier.size(18.dp))
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
                        .testTag("category_search_input")
                )
            }

            // Filter Buttons for Default Storage Locations
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(dynamicLocations) { loc ->
                        val isSelected = selectedLocationFilter == loc
                        val count = if (loc == "All") {
                            uiState.categories.size
                        } else {
                            uiState.categories.count { it.defaultLocation.equals(loc, ignoreCase = true) }
                        }
                        val locDisplayName = if (loc == "All") allLabel else LocalizationManager.localizeLocation(loc, uiState.selectedLanguage)
                        val emoji = uiState.locations.find { it.name.equals(loc, ignoreCase = true) }?.iconEmoji ?: ""
                        val labelText = if (emoji.isNotBlank()) "$emoji $locDisplayName ($count)" else "$locDisplayName ($count)"

                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedLocationFilter = loc },
                            label = { Text(labelText, style = MaterialTheme.typography.labelSmall) },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("filter_chip_cat_loc_$loc")
                        )
                    }
                }
            }

            // List of Category Cards
            if (filteredCategories.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Category,
                                contentDescription = null,
                                modifier = Modifier.size(54.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Text(
                                text = strings.noItemsFound,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Button(
                                onClick = { viewModel.openAddCategoryDialog() },
                                modifier = Modifier.testTag("btn_add_category_empty_state")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(strings.addCategory)
                            }
                        }
                    }
                }
            } else {
                items(filteredCategories, key = { it.id }) { category ->
                    val matchingItemsCount = uiState.inventoryItems.count { it.category.equals(category.name, ignoreCase = true) }
                    val matchingShoppingCount = uiState.shoppingItems.count { it.category.equals(category.name, ignoreCase = true) && !it.isPurchased }

                    CategoryCard(
                        category = category,
                        inventoryCount = matchingItemsCount,
                        shoppingCount = matchingShoppingCount,
                        onEdit = { viewModel.openEditCategoryDialog(category) },
                        onDelete = { categoryToDelete = category },
                        onFilterInventory = {
                            viewModel.setCategoryFilter(category.name)
                            viewModel.selectTab(0) // Switch to inventory
                        }
                    )
                }
            }
        }
    }

    // Add / Edit Category Dialog
    if (uiState.showCategoryDialog) {
        CategoryCrudDialog(
            category = uiState.categoryToEdit,
            availableLocations = uiState.locations,
            onDismiss = { viewModel.dismissCategoryDialog() },
            onSave = { name, iconEmoji, description, colorHex, defaultLocation, id ->
                viewModel.saveCategory(
                    name = name,
                    iconEmoji = iconEmoji,
                    description = description,
                    colorHex = colorHex,
                    defaultLocation = defaultLocation,
                    id = id
                )
            },
            selectedLanguage = uiState.selectedLanguage
        )
    }

    // Delete Confirmation Dialog
    categoryToDelete?.let { category ->
        val matchingCount = uiState.inventoryItems.count { it.category.equals(category.name, ignoreCase = true) }
        AlertDialog(
            onDismissRequest = { categoryToDelete = null },
            icon = {
                Icon(
                    Icons.Default.DeleteOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = "${strings.deleteCategory}: ${category.name}",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(strings.confirmDeleteCategory)
                    if (matchingCount > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⚠️ $matchingCount active inventory items currently tagged under '${category.name}'. ${strings.deleteCategoryWarning}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteCategory(category)
                        categoryToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("btn_confirm_delete_category")
                ) {
                    Text(strings.delete)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { categoryToDelete = null },
                    modifier = Modifier.testTag("btn_cancel_delete_category")
                ) {
                    Text(strings.cancel)
                }
            }
        )
    }
}

/**
 * Reusable CategoryCard component with full CRUD affordances,
 * item counts, location badge, and responsive color styling.
 */
@Composable
fun CategoryCard(
    category: CategoryEntity,
    inventoryCount: Int,
    shoppingCount: Int,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onFilterInventory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = remember(category.colorHex) {
        try {
            Color(android.graphics.Color.parseColor(category.colorHex))
        } catch (e: Exception) {
            Color(0xFF10B981)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("category_card_${category.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    accentColor.copy(alpha = 0.6f),
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
            ),
            width = 1.5.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top row: Emoji Icon, Category Name, Badges & Actions
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
                    // Emoji avatar with colored glow
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = accentColor.copy(alpha = 0.15f),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.radialGradient(listOf(accentColor, accentColor.copy(alpha = 0.4f))),
                            width = 1.dp
                        ),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = category.iconEmoji,
                                fontSize = 26.sp
                            )
                        }
                    }

                    Column {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = category.defaultLocation,
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
                            .testTag("btn_edit_category_${category.id}")
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Category",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_delete_category_${category.id}")
                    ) {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = "Delete Category",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Description if present
            if (category.description.isNotBlank()) {
                Text(
                    text = category.description,
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
                        text = "📦 $inventoryCount items",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (shoppingCount > 0) {
                        Text(
                            text = "• 🛒 $shoppingCount to buy",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                TextButton(
                    onClick = onFilterInventory,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.testTag("btn_filter_inventory_${category.id}")
                ) {
                    Text(
                        text = "View Items →",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/**
 * Material 3 Dialog for Creating or Updating a Category (CRUD)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCrudDialog(
    category: CategoryEntity?,
    availableLocations: List<com.example.data.local.entity.LocationEntity> = emptyList(),
    onDismiss: () -> Unit,
    onSave: (name: String, iconEmoji: String, description: String, colorHex: String, defaultLocation: String, id: Long) -> Unit,
    selectedLanguage: String
) {
    val strings = LocalizationManager.getStrings(selectedLanguage)
    val isEditing = category != null

    val locationList = if (availableLocations.isNotEmpty()) {
        availableLocations.map { it.name }
    } else {
        listOf("Fridge", "Pantry", "Freezer", "Spice Rack", "Countertop", "Wine & Cellar")
    }

    var name by remember { mutableStateOf(category?.name ?: "") }
    var iconEmoji by remember { mutableStateOf(category?.iconEmoji ?: "🍎") }
    var description by remember { mutableStateOf(category?.description ?: "") }
    var colorHex by remember { mutableStateOf(category?.colorHex ?: "#10B981") }
    var defaultLocation by remember { mutableStateOf(category?.defaultLocation ?: (locationList.firstOrNull() ?: "Pantry")) }
    var nameError by remember { mutableStateOf(false) }

    val presetEmojis = listOf(
        "🍎", "🥦", "🥛", "🥩", "☕", "🍿", "❄️", "🧴", "🍞", "🧂", "🧀", "🍳", "🍕", "🥗", "🍇", "🥕", "🥑", "🍪", "🧃", "🧺", "🍽️", "🥫", "✨", "🏷️", "📦"
    )

    val presetColors = listOf(
        "#10B981", // Emerald
        "#22C55E", // Green
        "#3B82F6", // Blue
        "#F59E0B", // Amber
        "#EF4444", // Red
        "#8B5CF6", // Purple
        "#EC4899", // Pink
        "#06B6D4", // Cyan
        "#64748B", // Slate
        "#D97706"  // Warm Orange
    )

    val locations = locationList

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    if (isEditing) Icons.Default.Edit else Icons.Default.AddCircleOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (isEditing) strings.editCategory else strings.addCategory,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 450.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Category Name Input
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            if (nameError) nameError = false
                        },
                        label = { Text(strings.categoryName + " *") },
                        isError = nameError,
                        supportingText = {
                            if (nameError) Text("Name cannot be empty", color = MaterialTheme.colorScheme.error)
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_category_name")
                    )
                }

                // Emoji Picker Section
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "${strings.categoryIcon}: $iconEmoji",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(presetEmojis) { emoji ->
                                val isSelected = emoji == iconEmoji
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clickable { iconEmoji = emoji }
                                        .testTag("emoji_picker_$emoji")
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = emoji, fontSize = 20.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // Color Theme Picker
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = strings.categoryColor,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(presetColors) { hex ->
                                val color = try { Color(android.graphics.Color.parseColor(hex)) } catch (e: Exception) { Color.Green }
                                val isSelected = hex.equals(colorHex, ignoreCase = true)
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .clickable { colorHex = hex }
                                        .testTag("color_picker_$hex"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Default Storage Location Picker
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = strings.defaultLocationLabel,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(locations) { loc ->
                                val isSelected = defaultLocation == loc
                                val emoji = availableLocations.find { it.name.equals(loc, ignoreCase = true) }?.iconEmoji ?: ""
                                val locLabel = if (emoji.isNotBlank()) "$emoji $loc" else loc
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { defaultLocation = loc },
                                    label = { Text(locLabel, style = MaterialTheme.typography.labelSmall) },
                                    modifier = Modifier.testTag("chip_location_$loc")
                                )
                            }
                        }
                    }
                }

                // Description Input
                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(strings.categoryDescription) },
                        maxLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_category_description")
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        nameError = true
                    } else {
                        onSave(
                            name,
                            iconEmoji,
                            description,
                            colorHex,
                            defaultLocation,
                            category?.id ?: 0L
                        )
                    }
                },
                modifier = Modifier.testTag("btn_save_category")
            ) {
                Text(if (isEditing) strings.save else strings.create)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("btn_cancel_category")
            ) {
                Text(strings.cancel)
            }
        }
    )
}
