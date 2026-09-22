package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ShoppingItemEntity
import com.example.ui.components.FamilyMemberAvatar
import com.example.ui.components.OracleCloudStatusBadge
import com.example.ui.i18n.LocalizationManager
import com.example.ui.theme.*
import com.example.ui.viewmodel.PantryUiState
import com.example.ui.viewmodel.PantryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    uiState: PantryUiState,
    viewModel: PantryViewModel,
    modifier: Modifier = Modifier
) {
    val strings = LocalizationManager.getStrings(uiState.selectedLanguage)
    var shoppingSearchQuery by remember { mutableStateOf("") }

    val memberNames = remember(uiState.familyMembers) {
        if (uiState.familyMembers.isNotEmpty()) uiState.familyMembers.map { it.name } else listOf("Mom", "Dad", "Maya", "Alex")
    }
    val familyMembersFilterList = remember(memberNames) {
        listOf("All") + memberNames
    }

    val allLabel = when (uiState.selectedLanguage.lowercase()) {
        "de" -> "Alle"
        "it" -> "Tutti"
        "pt" -> "Todos"
        else -> "All"
    }

    val filteredItems = remember(uiState.shoppingItems, uiState.selectedFamilyMemberFilter, shoppingSearchQuery) {
        uiState.shoppingItems.filter { item ->
            val matchMember = uiState.selectedFamilyMemberFilter == "All" ||
                    item.addedByFamilyMember.equals(uiState.selectedFamilyMemberFilter, ignoreCase = true)
            val matchQuery = shoppingSearchQuery.isBlank() ||
                    item.name.contains(shoppingSearchQuery, ignoreCase = true) ||
                    item.category.contains(shoppingSearchQuery, ignoreCase = true)
            matchMember && matchQuery
        }
    }

    val purchasedCount = filteredItems.count { it.isPurchased }
    val totalEstimatedPrice = filteredItems.filter { !it.isPurchased }.sumOf { it.estimatedPrice * it.quantity }

    val shoppingAsLabel = when (uiState.selectedLanguage.lowercase()) {
        "de" -> "Einkaufen als:"
        "it" -> "Spesa come:"
        "pt" -> "Comprando como:"
        else -> "Shopping as:"
    }

    val pendingLabel = when (uiState.selectedLanguage.lowercase()) {
        "de" -> "Offene Artikel"
        "it" -> "In Sospeso"
        "pt" -> "Itens Pendentes"
        else -> "Pending Items"
    }

    val estimatedTotalLabel = when (uiState.selectedLanguage.lowercase()) {
        "de" -> "Geschätzte Summe"
        "it" -> "Totale Stimato"
        "pt" -> "Total Estimado"
        else -> "Estimated Total"
    }

    Scaffold(
        modifier = modifier.testTag("shopping_list_screen"),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.setShowAddShoppingDialog(true) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("fab_add_shopping_item")
            ) {
                Icon(Icons.Default.AddShoppingCart, contentDescription = strings.addShoppingItem)
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
            // Header: Summary Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
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
                                        Icons.Default.ShoppingCart,
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
                                    text = strings.navShopping,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Summary Metrics Bar
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
                                        pendingLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        "${filteredItems.size - purchasedCount}",
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
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
                                        estimatedTotalLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        "${uiState.currencySymbol}${"%.2f".format(totalEstimatedPrice)}",
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        color = AmberSecondaryLight
                                    )
                                }
                            }
                        }

                        // Shopping As Selector
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                shoppingAsLabel,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(memberNames) { memberName ->
                                    val isCurrent = uiState.activeFamilyMember.equals(memberName, ignoreCase = true)
                                    val memberEntity = uiState.familyMembers.find { it.name.equals(memberName, ignoreCase = true) }
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { viewModel.setActiveFamilyMember(memberName) }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            FamilyMemberAvatar(
                                                name = memberName,
                                                avatarEmoji = memberEntity?.avatarEmoji,
                                                colorHex = memberEntity?.colorHex,
                                                size = 16
                                            )
                                            Text(
                                                text = memberName,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                                                ),
                                                color = if (isCurrent) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Compact Search Bar
            item {
                OutlinedTextField(
                    value = shoppingSearchQuery,
                    onValueChange = { shoppingSearchQuery = it },
                    placeholder = { Text(strings.searchPlaceholder, style = MaterialTheme.typography.bodyMedium) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (shoppingSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { shoppingSearchQuery = "" }) {
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
                        .padding(horizontal = 16.dp)
                        .testTag("search_shopping_input")
                )
            }

            // Filter Chips by Family Member
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(familyMembersFilterList) { member ->
                        val displayMember = if (member == "All") allLabel else if (member == "Oracle 26AI Auto-Restock") "Auto-Restock" else member
                        val memberEntity = uiState.familyMembers.find { it.name.equals(member, ignoreCase = true) }
                        FilterChip(
                            selected = uiState.selectedFamilyMemberFilter.equals(member, ignoreCase = true),
                            onClick = { viewModel.setFamilyMemberFilter(member) },
                            label = { Text(displayMember) },
                            leadingIcon = if (member != "All") {
                                {
                                    FamilyMemberAvatar(
                                        name = member,
                                        avatarEmoji = memberEntity?.avatarEmoji,
                                        colorHex = memberEntity?.colorHex,
                                        size = 18
                                    )
                                }
                            } else null
                        )
                    }
                }
            }

            // Actions bar (Clear Completed)
            if (purchasedCount > 0) {
                item {
                    val completedLabel = when (uiState.selectedLanguage.lowercase()) {
                        "de" -> "$purchasedCount erledigte Artikel"
                        "it" -> "$purchasedCount articoli completati"
                        "pt" -> "$purchasedCount itens concluídos"
                        else -> "$purchasedCount completed items"
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            completedLabel,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        TextButton(onClick = { viewModel.clearCompletedShopping() }) {
                            Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(strings.clearPurchased)
                        }
                    }
                }
            }

            // Shopping Items
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
                                Icons.Outlined.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Text(
                                strings.shoppingEmpty,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                strings.shoppingEmptyPrompt,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            } else {
                items(filteredItems, key = { it.id }) { item ->
                    ShoppingItemRow(
                        item = item,
                        languageCode = uiState.selectedLanguage,
                        currencySymbol = uiState.currencySymbol,
                        onTogglePurchased = { viewModel.toggleShoppingItem(item) },
                        onEdit = { viewModel.openEditShoppingDialog(item) },
                        onRestockInventory = { viewModel.markBoughtAndRestock(item) },
                        onDelete = { viewModel.deleteShoppingItem(item) }
                    )
                }
            }
        }
    }

    // Add Shopping Item Dialog
    if (uiState.showAddShoppingDialog) {
        AddShoppingItemDialog(
            languageCode = uiState.selectedLanguage,
            currencySymbol = uiState.currencySymbol,
            availableCategories = uiState.categories,
            onDismiss = { viewModel.setShowAddShoppingDialog(false) },
            onConfirm = { name, cat, qty, unit, priority, price ->
                viewModel.addShoppingItem(name, cat, qty, unit, priority, price)
            }
        )
    }

    // Edit Shopping Item Dialog
    if (uiState.showEditShoppingDialog && uiState.shoppingItemToEdit != null) {
        EditShoppingItemDialog(
            item = uiState.shoppingItemToEdit,
            languageCode = uiState.selectedLanguage,
            currencySymbol = uiState.currencySymbol,
            availableCategories = uiState.categories,
            onDismiss = { viewModel.dismissEditShoppingDialog() },
            onConfirm = { id, name, cat, qty, unit, priority, price, requestedBy ->
                viewModel.updateShoppingItem(id, name, cat, qty, unit, priority, price, requestedBy)
            }
        )
    }
}

@Composable
fun ShoppingItemRow(
    item: ShoppingItemEntity,
    languageCode: String,
    currencySymbol: String = "$",
    onTogglePurchased: () -> Unit,
    onEdit: () -> Unit,
    onRestockInventory: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalizationManager.getStrings(languageCode)
    val displayName = LocalizationManager.localizeItemName(item.name, languageCode)
    val restockBtnLabel = when (languageCode.lowercase()) {
        "de" -> "Auffüllen"
        "it" -> "Rifornisci"
        "pt" -> "Repor"
        else -> "Restock"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("shopping_item_${item.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isPurchased) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (item.isPurchased) 0.dp else 1.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Checkbox(
                checked = item.isPurchased,
                onCheckedChange = { onTogglePurchased() },
                modifier = Modifier.testTag("checkbox_${item.id}")
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (item.isPurchased) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = if (item.isPurchased) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${item.quantity} ${item.unit}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("•", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    Text(
                        text = "$currencySymbol${"%.2f".format(item.estimatedPrice)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    FamilyMemberAvatar(name = item.addedByFamilyMember, size = 18)
                    Text(
                        text = "${strings.requestedBy} ${item.addedByFamilyMember}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (item.priority == "HIGH") {
                        Surface(
                            color = StatusCritical.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                strings.urgent,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusCritical
                                )
                            )
                        }
                    }
                }
            }

            // Edit button
            IconButton(
                onClick = onEdit,
                modifier = Modifier.size(32.dp).testTag("edit_shopping_item_${item.id}")
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit Item",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }

            // 1-tap "Mark Bought & Restock Pantry" or Delete
            if (!item.isPurchased) {
                FilledTonalButton(
                    onClick = onRestockInventory,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        Icons.Default.AddBusiness,
                        contentDescription = "Bought & Restock",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(restockBtnLabel, fontSize = 11.sp)
                }
            } else {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Close, contentDescription = "Remove", tint = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShoppingItemDialog(
    languageCode: String,
    currencySymbol: String = "$",
    availableCategories: List<com.example.data.local.entity.CategoryEntity> = emptyList(),
    onDismiss: () -> Unit,
    onConfirm: (name: String, category: String, qty: Double, unit: String, priority: String, price: Double) -> Unit
) {
    val strings = LocalizationManager.getStrings(languageCode)
    val categoryList = if (availableCategories.isNotEmpty()) {
        availableCategories.map { it.name }
    } else {
        listOf("Produce", "Dairy & Eggs", "Pantry", "Meat & Seafood", "Beverages", "Snacks", "Bakery")
    }

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(categoryList.firstOrNull() ?: "Produce") }
    var quantityStr by remember { mutableStateOf("1.0") }
    var unit by remember { mutableStateOf("units") }
    var priority by remember { mutableStateOf("NORMAL") }
    var priceStr by remember { mutableStateOf("3.99") }

    val priorities = listOf("HIGH", "NORMAL", "LOW")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.addShoppingItem) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(strings.itemName) },
                    placeholder = { Text("e.g., Cold Brew Coffee") },
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

                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it },
                    label = { Text("Est. Price ($currencySymbol)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                val priorityLabel = when (languageCode.lowercase()) {
                    "de" -> "Priorität"
                    "it" -> "Priorità"
                    "pt" -> "Prioridade"
                    else -> "Priority"
                }
                Text(priorityLabel, style = MaterialTheme.typography.labelSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    priorities.forEach { p ->
                        val pLabel = when (p) {
                            "HIGH" -> strings.urgent
                            "NORMAL" -> when (languageCode.lowercase()) {
                                "de" -> "Normal"
                                "it" -> "Normale"
                                "pt" -> "Normal"
                                else -> "Normal"
                            }
                            "LOW" -> when (languageCode.lowercase()) {
                                "de" -> "Niedrig"
                                "it" -> "Basso"
                                "pt" -> "Baixa"
                                else -> "Low"
                            }
                            else -> p
                        }
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(pLabel, fontSize = 11.sp) }
                        )
                    }
                }

                Text(strings.category, style = MaterialTheme.typography.labelSmall)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categoryList) { cat ->
                        val emoji = availableCategories.find { it.name.equals(cat, true) }?.iconEmoji ?: ""
                        val labelText = if (emoji.isNotBlank()) "$emoji ${LocalizationManager.localizeCategory(cat, languageCode)}" else LocalizationManager.localizeCategory(cat, languageCode)
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(labelText, fontSize = 11.sp) }
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
                        val price = priceStr.toDoubleOrNull() ?: 3.99
                        onConfirm(name, category, qty, unit, priority, price)
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
fun EditShoppingItemDialog(
    item: ShoppingItemEntity,
    languageCode: String,
    currencySymbol: String = "$",
    availableCategories: List<com.example.data.local.entity.CategoryEntity> = emptyList(),
    onDismiss: () -> Unit,
    onConfirm: (id: Long, name: String, category: String, qty: Double, unit: String, priority: String, price: Double, requestedBy: String) -> Unit
) {
    val strings = LocalizationManager.getStrings(languageCode)
    val categoryList = if (availableCategories.isNotEmpty()) {
        availableCategories.map { it.name }
    } else {
        listOf("Produce", "Dairy & Eggs", "Pantry", "Meat & Seafood", "Beverages", "Snacks", "Bakery")
    }

    var name by remember { mutableStateOf(item.name) }
    var category by remember { mutableStateOf(if (categoryList.contains(item.category)) item.category else (categoryList.firstOrNull() ?: "Produce")) }
    var quantityStr by remember { mutableStateOf(item.quantity.toString()) }
    var unit by remember { mutableStateOf(item.unit) }
    var priority by remember { mutableStateOf(item.priority) }
    var priceStr by remember { mutableStateOf(item.estimatedPrice.toString()) }
    var requestedBy by remember { mutableStateOf(item.addedByFamilyMember) }

    val priorities = listOf("HIGH", "NORMAL", "LOW")

    val editTitle = when (languageCode.lowercase()) {
        "de" -> "Einkaufsartikel bearbeiten"
        "it" -> "Modifica elemento della spesa"
        "pt" -> "Editar item de compras"
        else -> "Edit Shopping Item"
    }

    val saveText = when (languageCode.lowercase()) {
        "de" -> "Speichern"
        "it" -> "Salva"
        "pt" -> "Salvar"
        else -> "Save"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(editTitle) },
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

                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it },
                    label = { Text("Est. Price ($currencySymbol)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                val priorityLabel = when (languageCode.lowercase()) {
                    "de" -> "Priorität"
                    "it" -> "Priorità"
                    "pt" -> "Prioridade"
                    else -> "Priority"
                }
                Text(priorityLabel, style = MaterialTheme.typography.labelSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    priorities.forEach { p ->
                        val pLabel = when (p) {
                            "HIGH" -> strings.urgent
                            "NORMAL" -> when (languageCode.lowercase()) {
                                "de" -> "Normal"
                                "it" -> "Normale"
                                "pt" -> "Normal"
                                else -> "Normal"
                            }
                            "LOW" -> when (languageCode.lowercase()) {
                                "de" -> "Niedrig"
                                "it" -> "Basso"
                                "pt" -> "Baixa"
                                else -> "Low"
                            }
                            else -> p
                        }
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(pLabel, fontSize = 11.sp) }
                        )
                    }
                }

                Text(strings.category, style = MaterialTheme.typography.labelSmall)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categoryList) { cat ->
                        val emoji = availableCategories.find { it.name.equals(cat, true) }?.iconEmoji ?: ""
                        val labelText = if (emoji.isNotBlank()) "$emoji ${LocalizationManager.localizeCategory(cat, languageCode)}" else LocalizationManager.localizeCategory(cat, languageCode)
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(labelText, fontSize = 11.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val qty = quantityStr.toDoubleOrNull() ?: item.quantity
                        val price = priceStr.toDoubleOrNull() ?: item.estimatedPrice
                        onConfirm(item.id, name, category, qty, unit, priority, price, requestedBy)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text(saveText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(strings.cancel)
            }
        }
    )
}
