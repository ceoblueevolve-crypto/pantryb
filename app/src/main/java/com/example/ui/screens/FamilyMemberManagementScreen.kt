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
import com.example.data.local.entity.FamilyMemberEntity
import com.example.ui.components.FamilyMemberAvatar
import com.example.ui.i18n.LocalizationManager
import com.example.ui.theme.*
import com.example.ui.viewmodel.AVAILABLE_LANGUAGES
import com.example.ui.viewmodel.PantryUiState
import com.example.ui.viewmodel.PantryViewModel

private val PRESET_FAMILY_EMOJIS = listOf(
    "👩", "👨", "👧", "👦", "👵", "👴", "🧑‍🦱", "👱‍♀️", "🧔", "🧑‍🍳", "👶", "🐱", "🐶", "🧑‍🎓", "👩‍💻", "🏃‍♀️", "🧘", "🥑"
)

private val PRESET_FAMILY_COLORS = listOf(
    "#EC4899", // Pink (Mom)
    "#3B82F6", // Blue (Dad)
    "#8B5CF6", // Purple (Maya)
    "#F59E0B", // Amber (Alex)
    "#10B981", // Emerald
    "#06B6D4", // Cyan
    "#EF4444", // Red
    "#D97706", // Orange
    "#6366F1", // Indigo
    "#64748B"  // Slate
)

private val PRESET_ROLES = listOf(
    "Parent / Admin",
    "Parent",
    "Teen / Student",
    "Child",
    "Grandparent",
    "Roommate",
    "Guest"
)

private val PRESET_DIETARY_TAGS = listOf(
    "🌱 Vegetarian",
    "🥗 Vegan",
    "🌾 Gluten-Free",
    "🥜 Peanut Allergy",
    "🥛 Dairy-Free",
    "🥩 Low-Carb / Keto",
    "☕ Espresso lover",
    "🥑 Organic preferred"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyMemberManagementScreen(
    uiState: PantryUiState,
    viewModel: PantryViewModel,
    modifier: Modifier = Modifier
) {
    val strings = LocalizationManager.getStrings(uiState.selectedLanguage)
    var memberSearchQuery by remember { mutableStateOf("") }
    var memberToDelete by remember { mutableStateOf<FamilyMemberEntity?>(null) }
    var selectedRoleFilter by remember { mutableStateOf<String?>(null) }

    val filteredMembers = remember(uiState.familyMembers, memberSearchQuery, selectedRoleFilter) {
        uiState.familyMembers.filter { member ->
            val matchesQuery = memberSearchQuery.isBlank() ||
                member.name.contains(memberSearchQuery, ignoreCase = true) ||
                member.role.contains(memberSearchQuery, ignoreCase = true) ||
                member.dietaryNotes.contains(memberSearchQuery, ignoreCase = true) ||
                member.preferredLanguage.contains(memberSearchQuery, ignoreCase = true)
            val matchesRole = selectedRoleFilter == null || member.role.contains(selectedRoleFilter.orEmpty(), ignoreCase = true)
            matchesQuery && matchesRole
        }
    }

    val totalMembers = uiState.familyMembers.size
    val activeShoppingRequests = uiState.shoppingItems.filter { !it.isPurchased }.size
    val dietaryAlertsCount = uiState.familyMembers.count { it.dietaryNotes.isNotBlank() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.openAddFamilyMemberDialog() },
                icon = { Icon(Icons.Default.Add, contentDescription = strings.addFamilyMember) },
                text = { Text(strings.addFamilyMember, fontWeight = FontWeight.Bold) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("fab_add_family_member")
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Hero Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = strings.familyTitle,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "${strings.activeProfile}: ${uiState.activeFamilyMember}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.People,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        // Statistics Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "$totalMembers",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Members",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "$activeShoppingRequests",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF10B981)
                                    )
                                    Text(
                                        text = "Shopping Items",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "$dietaryAlertsCount",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF59E0B)
                                    )
                                    Text(
                                        text = "Dietary Tags",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Search Bar & Filter Row
            item {
                OutlinedTextField(
                    value = memberSearchQuery,
                    onValueChange = { memberSearchQuery = it },
                    placeholder = { Text("Search family members, diets, roles...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
                    },
                    trailingIcon = {
                        if (memberSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { memberSearchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_search_family_member"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }

            // Filter Chips Row
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedRoleFilter == null,
                            onClick = { selectedRoleFilter = null },
                            label = { Text(strings.allMembersLabel) },
                            leadingIcon = if (selectedRoleFilter == null) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null
                        )
                    }
                    items(listOf("Parent", "Teen", "Child", "Roommate")) { roleKey ->
                        FilterChip(
                            selected = selectedRoleFilter == roleKey,
                            onClick = {
                                selectedRoleFilter = if (selectedRoleFilter == roleKey) null else roleKey
                            },
                            label = { Text(roleKey) }
                        )
                    }
                }
            }

            // Member Cards List
            if (filteredMembers.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("👤", fontSize = 42.sp)
                            Text(
                                text = "No family members found",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Add household members to organize shared grocery shopping and dietary needs.",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Button(
                                onClick = { viewModel.openAddFamilyMemberDialog() },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(strings.addFamilyMember)
                            }
                        }
                    }
                }
            } else {
                items(filteredMembers, key = { it.id }) { member ->
                    val isActive = member.name.equals(uiState.activeFamilyMember, ignoreCase = true)
                    val memberShoppingCount = uiState.shoppingItems.count { it.addedByFamilyMember.equals(member.name, ignoreCase = true) }
                    val lang = AVAILABLE_LANGUAGES.find { it.code.equals(member.preferredLanguage, ignoreCase = true) }

                    FamilyMemberCard(
                        member = member,
                        isActive = isActive,
                        shoppingCount = memberShoppingCount,
                        languageFlag = lang?.flagEmoji ?: "🌐",
                        languageName = lang?.displayName ?: member.preferredLanguage.uppercase(),
                        onSetActive = { viewModel.setActiveFamilyMember(member.name) },
                        onEdit = { viewModel.openEditFamilyMemberDialog(member) },
                        onDelete = { memberToDelete = member }
                    )
                }
            }
        }
    }

    // Add / Edit Family Member Dialog
    if (uiState.showFamilyMemberDialog) {
        FamilyMemberCrudDialog(
            memberToEdit = uiState.familyMemberToEdit,
            onDismiss = { viewModel.dismissFamilyMemberDialog() },
            onSave = { name, role, emoji, color, dietary, language, isDefault, id ->
                viewModel.saveFamilyMember(name, role, emoji, color, dietary, language, isDefault, id)
            }
        )
    }

    // Delete Confirmation Dialog
    memberToDelete?.let { member ->
        val shoppingAssocCount = uiState.shoppingItems.count { it.addedByFamilyMember.equals(member.name, ignoreCase = true) }

        AlertDialog(
            onDismissRequest = { memberToDelete = null },
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
                    text = strings.confirmDeleteMember,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Are you sure you want to remove '${member.name}'?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (shoppingAssocCount > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⚠️ $shoppingAssocCount shopping item(s) are currently attributed to ${member.name}.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    Text(
                        text = strings.deleteMemberWarning,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteFamilyMember(member)
                        memberToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.testTag("btn_confirm_delete_family_member")
                ) {
                    Text(strings.delete)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { memberToDelete = null }) {
                    Text(strings.cancel)
                }
            }
        )
    }
}

@Composable
fun FamilyMemberCard(
    member: FamilyMemberEntity,
    isActive: Boolean,
    shoppingCount: Int,
    languageFlag: String,
    languageName: String,
    onSetActive: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val memberColor = remember(member.colorHex) {
        try {
            Color(android.graphics.Color.parseColor(member.colorHex))
        } catch (e: Exception) {
            Color(0xFF3B82F6)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("card_family_member_${member.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isActive) 3.dp else 1.5.dp),
        border = if (isActive) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row: Avatar, Name + Role, Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Avatar Box
                FamilyMemberAvatar(
                    name = member.name,
                    avatarEmoji = member.avatarEmoji,
                    colorHex = member.colorHex,
                    size = 46
                )

                // Name & Role
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = member.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (isActive) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 2.dp)
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.ExtraBold),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        } else if (member.isDefault) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.padding(start = 2.dp)
                            ) {
                                Text(
                                    text = "DEFAULT",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = member.role,
                        style = MaterialTheme.typography.bodySmall,
                        color = memberColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Edit & Delete Action Icons
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(36.dp).testTag("btn_edit_family_member_${member.id}")
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(36.dp).testTag("btn_delete_family_member_${member.id}")
                    ) {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Dietary Notes / Allergies (if present)
            if (member.dietaryNotes.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = memberColor.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Outlined.RestaurantMenu,
                            contentDescription = null,
                            tint = memberColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = member.dietaryNotes,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Footer Badges: Language preference & Shopping items count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Language Chip
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(languageFlag, fontSize = 12.sp)
                            Text(
                                text = languageName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Shopping Request Count
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "$shoppingCount requested",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                if (!isActive) {
                    OutlinedButton(
                        onClick = onSetActive,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(30.dp).testTag("btn_set_active_family_member_${member.id}")
                    ) {
                        Text("Switch Profile", style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyMemberCrudDialog(
    memberToEdit: FamilyMemberEntity?,
    onDismiss: () -> Unit,
    onSave: (name: String, role: String, emoji: String, color: String, dietary: String, language: String, isDefault: Boolean, id: Long) -> Unit
) {
    var name by remember { mutableStateOf(memberToEdit?.name.orEmpty()) }
    var role by remember { mutableStateOf(memberToEdit?.role ?: "Parent") }
    var selectedEmoji by remember { mutableStateOf(memberToEdit?.avatarEmoji ?: "👩") }
    var selectedColor by remember { mutableStateOf(memberToEdit?.colorHex ?: "#EC4899") }
    var dietaryNotes by remember { mutableStateOf(memberToEdit?.dietaryNotes.orEmpty()) }
    var preferredLanguage by remember { mutableStateOf(memberToEdit?.preferredLanguage ?: "en") }
    var isDefault by remember { mutableStateOf(memberToEdit?.isDefault ?: false) }

    var nameError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (memberToEdit == null) "Add Family Member" else "Edit Family Member",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Live Profile Preview
                item {
                    val previewColor = remember(selectedColor) {
                        try {
                            Color(android.graphics.Color.parseColor(selectedColor))
                        } catch (e: Exception) {
                            Color(0xFF3B82F6)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = previewColor.copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, previewColor.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            FamilyMemberAvatar(
                                name = if (name.isBlank()) "User" else name,
                                avatarEmoji = selectedEmoji,
                                colorHex = selectedColor,
                                size = 44
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (name.isBlank()) "Member Name" else name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$role • ${AVAILABLE_LANGUAGES.find { it.code == preferredLanguage }?.flagEmoji ?: "🌐"} $preferredLanguage",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = previewColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                // Name Input
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            if (it.isNotBlank()) nameError = false
                        },
                        label = { Text("Name / Nickname *") },
                        isError = nameError,
                        supportingText = if (nameError) {
                            { Text("Please enter a name", color = MaterialTheme.colorScheme.error) }
                        } else null,
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_family_member_name")
                    )
                }

                // Role Selection
                item {
                    Text(
                        text = "Household Role / Relationship",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        items(PRESET_ROLES) { presetRole ->
                            FilterChip(
                                selected = role == presetRole,
                                onClick = { role = presetRole },
                                label = { Text(presetRole, fontSize = 12.sp) }
                            )
                        }
                    }
                }

                // Avatar / Emoji Picker
                item {
                    Text(
                        text = "Choose Avatar Emoji",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        items(PRESET_FAMILY_EMOJIS) { emoji ->
                            val isSelected = selectedEmoji == emoji
                            Surface(
                                shape = CircleShape,
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .clickable { selectedEmoji = emoji }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = emoji, fontSize = 22.sp)
                                }
                            }
                        }
                    }
                }

                // Color Picker
                item {
                    Text(
                        text = "Accent / Profile Color",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        items(PRESET_FAMILY_COLORS) { colorHex ->
                            val isSelected = selectedColor.equals(colorHex, ignoreCase = true)
                            val color = try {
                                Color(android.graphics.Color.parseColor(colorHex))
                            } catch (e: Exception) {
                                Color.Gray
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .clickable { selectedColor = colorHex }
                                    .then(
                                        if (isSelected) {
                                            Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                        } else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Dietary Preferences & Allergies Input
                item {
                    OutlinedTextField(
                        value = dietaryNotes,
                        onValueChange = { dietaryNotes = it },
                        label = { Text("Dietary Preferences & Allergies") },
                        placeholder = { Text("e.g. Vegetarian, Peanut allergy, Lactose-free") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_family_member_dietary")
                    )

                    // Quick dietary tag pills
                    Text(
                        text = "Quick Tag Suggestions:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        items(PRESET_DIETARY_TAGS) { tag ->
                            SuggestionChip(
                                onClick = {
                                    val cleanedTag = tag.substringAfter(" ")
                                    dietaryNotes = if (dietaryNotes.isBlank()) {
                                        cleanedTag
                                    } else if (!dietaryNotes.contains(cleanedTag, ignoreCase = true)) {
                                        "$dietaryNotes, $cleanedTag"
                                    } else dietaryNotes
                                },
                                label = { Text(tag, fontSize = 11.sp) }
                            )
                        }
                    }
                }

                // Preferred Language
                item {
                    Text(
                        text = "Preferred Language",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        items(AVAILABLE_LANGUAGES.take(6)) { lang ->
                            FilterChip(
                                selected = preferredLanguage.equals(lang.code, ignoreCase = true),
                                onClick = { preferredLanguage = lang.code },
                                label = { Text("${lang.flagEmoji} ${lang.displayName}", fontSize = 12.sp) }
                            )
                        }
                    }
                }

                // Default Profile Switch
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Default Profile",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Automatically select this profile on launch",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = isDefault,
                            onCheckedChange = { isDefault = it },
                            modifier = Modifier.testTag("switch_family_member_default")
                        )
                    }
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
                            role,
                            selectedEmoji,
                            selectedColor,
                            dietaryNotes,
                            preferredLanguage,
                            isDefault,
                            memberToEdit?.id ?: 0L
                        )
                    }
                },
                modifier = Modifier.testTag("btn_save_family_member")
            ) {
                Text(if (memberToEdit == null) "Add Member" else "Save Changes")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
