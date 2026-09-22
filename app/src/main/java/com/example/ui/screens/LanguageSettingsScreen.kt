package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.i18n.LocalizationManager
import com.example.ui.viewmodel.AVAILABLE_LANGUAGES
import com.example.ui.viewmodel.PantryUiState
import com.example.ui.viewmodel.PantryViewModel
import com.example.ui.viewmodel.SupportedLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSettingsScreen(
    uiState: PantryUiState,
    viewModel: PantryViewModel,
    modifier: Modifier = Modifier
) {
    val strings = LocalizationManager.getStrings(uiState.selectedLanguage)
    val currentLang = AVAILABLE_LANGUAGES.find { it.code == uiState.selectedLanguage }
        ?: AVAILABLE_LANGUAGES.first()

    var showMemberLanguageDialog by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("language_settings_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Banner / Introduction
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(52.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Language,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = strings.preferredLanguageCommunications,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = strings.langSetupSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Available Languages Selector Header & Section (PRIMARY & AT TOP)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = strings.preferredLanguageCommunications,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = when (uiState.selectedLanguage.lowercase()) {
                        "de" -> "Wählen Sie Ihre bevorzugte Sprache für die gesamte Benutzeroberfläche:"
                        "it" -> "Seleziona la tua lingua preferita per l'intera interfaccia dell'app:"
                        "pt" -> "Selecione o seu idioma de preferência para toda a interface do app:"
                        else -> "Select your preferred language for the entire app interface:"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // List of all supported languages (Prominent Clickable Cards)
        items(AVAILABLE_LANGUAGES) { language ->
            val isSelected = uiState.selectedLanguage == language.code

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                else
                    MaterialTheme.colorScheme.surface,
                tonalElevation = if (isSelected) 4.dp else 1.dp,
                shadowElevation = if (isSelected) 2.dp else 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { viewModel.setLanguage(language.code) }
                    .testTag("language_option_${language.code}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = language.flagEmoji,
                            fontSize = 30.sp
                        )
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = language.nativeName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                if (isSelected) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primary
                                    ) {
                                        Text(
                                            text = "ACTIVE",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "${language.displayName} • ${language.region}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    RadioButton(
                        selected = isSelected,
                        onClick = { viewModel.setLanguage(language.code) },
                        modifier = Modifier.testTag("radio_language_${language.code}")
                    )
                }
            }
        }

        // Active Language & System Auto-detect
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = strings.activeInterfaceLang,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = currentLang.flagEmoji,
                                    fontSize = 26.sp
                                )
                                Text(
                                    text = currentLang.nativeName,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "(${currentLang.displayName})",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = currentLang.code.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Live sample greeting banner
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                Icons.Default.ChatBubbleOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "\"${currentLang.sampleGreeting}\"",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // Auto-detect toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = strings.autoDetectSystem,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (uiState.autoDetectSystemLanguage)
                                    strings.autoDetectSynced
                                else
                                    strings.autoDetectManual,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = uiState.autoDetectSystemLanguage,
                            onCheckedChange = { viewModel.setAutoDetectSystemLanguage(it) },
                            modifier = Modifier.testTag("auto_detect_language_switch")
                        )
                    }
                }
            }
        }

        // [WAITING_NEW_VERSION] Oracle 26AI Cross-Language Panel
        // item {
        //     Card(
        //         colors = CardDefaults.cardColors(
        //             containerColor = MaterialTheme.colorScheme.surface
        //         ),
        //         elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        //         shape = RoundedCornerShape(16.dp),
        //         modifier = Modifier.fillMaxWidth()
        //     ) {
        //         Column(
        //             modifier = Modifier
        //                 .fillMaxWidth()
        //                 .padding(16.dp),
        //             verticalArrangement = Arrangement.spacedBy(12.dp)
        //         ) {
        //             Row(
        //                 modifier = Modifier.fillMaxWidth(),
        //                 verticalAlignment = Alignment.CenterVertically,
        //                 horizontalArrangement = Arrangement.SpaceBetween
        //             ) {
        //                 Row(
        //                     verticalAlignment = Alignment.CenterVertically,
        //                     horizontalArrangement = Arrangement.spacedBy(10.dp)
        //                 ) {
        //                     Icon(
        //                         Icons.Default.Translate,
        //                         contentDescription = null,
        //                         tint = MaterialTheme.colorScheme.secondary,
        //                         modifier = Modifier.size(22.dp)
        //                     )
        //                     Text(
        //                         text = strings.aiTranslationTitle,
        //                         style = MaterialTheme.typography.titleSmall,
        //                         fontWeight = FontWeight.Bold
        //                     )
        //                 }
        //                 Switch(
        //                     checked = uiState.enableAiAutoTranslation,
        //                     onCheckedChange = { viewModel.setAiAutoTranslation(it) },
        //                     modifier = Modifier.testTag("ai_translation_switch")
        //                 )
        //             }
        //
        //             Text(
        //                 text = strings.aiTranslationDesc,
        //                 style = MaterialTheme.typography.bodySmall,
        //                 color = MaterialTheme.colorScheme.onSurfaceVariant
        //             )
        //
        //             // Translation Preview Chip Box
        //             Surface(
        //                 shape = RoundedCornerShape(8.dp),
        //                 color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
        //                 modifier = Modifier.fillMaxWidth()
        //             ) {
        //                 Row(
        //                     modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        //                     verticalAlignment = Alignment.CenterVertically,
        //                     horizontalArrangement = Arrangement.spacedBy(8.dp)
        //                 ) {
        //                     Text(
        //                         text = strings.aiLiveDictionary,
        //                         style = MaterialTheme.typography.labelSmall,
        //                         fontWeight = FontWeight.Bold,
        //                         color = MaterialTheme.colorScheme.secondary
        //                     )
        //                     Text(
        //                         text = when (uiState.selectedLanguage) {
        //                             "es" -> "Almond Milk → Leche de Almendras"
        //                             "fr" -> "Almond Milk → Lait d'amande"
        //                             "de" -> "Almond Milk → Mandelmilch"
        //                             "it" -> "Almond Milk → Latte di Mandorla"
        //                             "pt" -> "Almond Milk → Leite de Amêndoas"
        //                             "ja" -> "Almond Milk → アーモンドミルク"
        //                             "zh" -> "Almond Milk → 杏仁奶"
        //                             "ko" -> "Almond Milk → 아몬드 밀크"
        //                             "ar" -> "Almond Milk → حليب اللوز"
        //                             else -> "Almond Milk → Dairy-Free Beverage"
        //                         },
        //                         style = MaterialTheme.typography.bodySmall,
        //                         fontWeight = FontWeight.Medium
        //                     )
        //                 }
        //             }
        //         }
        //     }
        // }

        // [WAITING_NEW_VERSION] Household Member Language Support Setup Panel
        // item {
        //     Card(
        //         colors = CardDefaults.cardColors(
        //             containerColor = MaterialTheme.colorScheme.surface
        //         ),
        //         elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        //         shape = RoundedCornerShape(16.dp),
        //         modifier = Modifier.fillMaxWidth()
        //     ) {
        //         Column(
        //             modifier = Modifier
        //                 .fillMaxWidth()
        //                 .padding(16.dp),
        //             verticalArrangement = Arrangement.spacedBy(12.dp)
        //         ) {
        //             Text(
        //                 text = strings.householdMembersTitle,
        //                 style = MaterialTheme.typography.titleSmall,
        //                 fontWeight = FontWeight.Bold
        //             )
        //             Text(
        //                 text = strings.householdMembersDesc,
        //                 style = MaterialTheme.typography.bodySmall,
        //                 color = MaterialTheme.colorScheme.onSurfaceVariant
        //             )
        //
        //             val memberList = if (uiState.familyMembers.isNotEmpty()) {
        //                 uiState.familyMembers.map { it.name }
        //             } else {
        //                 listOf("Mom", "Dad", "Teenager", "Kid")
        //             }
        //
        //             memberList.forEach { member ->
        //                 val memberEntity = uiState.familyMembers.find { it.name.equals(member, ignoreCase = true) }
        //                 val memberLangCode = uiState.familyMemberLanguages[member] ?: (memberEntity?.preferredLanguage ?: "en")
        //                 val memberLang = AVAILABLE_LANGUAGES.find { it.code == memberLangCode }
        //                     ?: AVAILABLE_LANGUAGES.first()
        //                 val memberDisplayName = LocalizationManager.localizeMember(member, uiState.selectedLanguage)
        //
        //                 val activeProfileText = when (uiState.selectedLanguage.lowercase()) {
        //                     "de" -> "Derzeit aktives Profil"
        //                     "it" -> "Profilo attualmente attivo"
        //                     "pt" -> "Perfil atualmente ativo"
        //                     else -> "Currently Active Profile"
        //                 }
        //                 val roleText = memberEntity?.role ?: when (uiState.selectedLanguage.lowercase()) {
        //                     "de" -> "Familienmitglied"
        //                     "it" -> "Membro della famiglia"
        //                     "pt" -> "Membro da família"
        //                     else -> "Family Member"
        //                 }
        //
        //                 Row(
        //                     modifier = Modifier
        //                         .fillMaxWidth()
        //                         .clip(RoundedCornerShape(10.dp))
        //                         .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        //                         .clickable { showMemberLanguageDialog = member }
        //                         .padding(horizontal = 14.dp, vertical = 10.dp),
        //                     verticalAlignment = Alignment.CenterVertically,
        //                     horizontalArrangement = Arrangement.SpaceBetween
        //                 ) {
        //                     Row(
        //                         verticalAlignment = Alignment.CenterVertically,
        //                         horizontalArrangement = Arrangement.spacedBy(10.dp)
        //                     ) {
        //                         com.example.ui.components.FamilyMemberAvatar(
        //                             name = member,
        //                             avatarEmoji = memberEntity?.avatarEmoji,
        //                             colorHex = memberEntity?.colorHex,
        //                             size = 36
        //                         )
        //                         Column {
        //                             Text(
        //                                 text = memberDisplayName,
        //                                 style = MaterialTheme.typography.bodyMedium,
        //                                 fontWeight = FontWeight.SemiBold
        //                             )
        //                             Text(
        //                                 text = if (member == uiState.activeFamilyMember) activeProfileText else roleText,
        //                                 style = MaterialTheme.typography.bodySmall,
        //                                 color = if (member == uiState.activeFamilyMember) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        //                             )
        //                         }
        //                     }
        //
        //                     Row(
        //                         verticalAlignment = Alignment.CenterVertically,
        //                         horizontalArrangement = Arrangement.spacedBy(6.dp)
        //                     ) {
        //                         Text(
        //                             text = memberLang.flagEmoji,
        //                             fontSize = 18.sp
        //                         )
        //                         Text(
        //                             text = memberLang.nativeName,
        //                             style = MaterialTheme.typography.bodySmall,
        //                             fontWeight = FontWeight.Medium
        //                         )
        //                         Icon(
        //                             Icons.Default.ChevronRight,
        //                             contentDescription = "Edit",
        //                             tint = MaterialTheme.colorScheme.onSurfaceVariant,
        //                             modifier = Modifier.size(18.dp)
        //                         )
        //                     }
        //                 }
        //             }
        //         }
        //     }
        // }

        // Regional Measurements & Currency Formats
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = strings.regionalUnitsTitle,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    // Measurement System
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = strings.unitOfMeasure,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = uiState.measurementSystem,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = uiState.measurementSystem.startsWith("Metric"),
                                onClick = { viewModel.setMeasurementSystem("Metric (kg, g, L)") },
                                label = { Text("Metric") }
                            )
                            FilterChip(
                                selected = uiState.measurementSystem.startsWith("Imperial"),
                                onClick = { viewModel.setMeasurementSystem("Imperial (lb, oz, fl oz)") },
                                label = { Text("Imperial") }
                            )
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // Currency Symbol
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = strings.currencyIndicator,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            val currentText = when (uiState.selectedLanguage.lowercase()) {
                                "de" -> "Aktuell: "
                                "it" -> "Attuale: "
                                "pt" -> "Atual: "
                                else -> "Current: "
                            }
                            Text(
                                text = "$currentText${uiState.currencySymbol}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        val currencies = listOf("$", "€", "£", "¥")
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            currencies.forEach { curr ->
                                FilterChip(
                                    selected = uiState.currencySymbol == curr,
                                    onClick = { viewModel.setCurrencySymbol(curr) },
                                    label = { Text(curr) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Member language selector dialog
    showMemberLanguageDialog?.let { member ->
        val memberDisplayName = LocalizationManager.localizeMember(member, uiState.selectedLanguage)
        AlertDialog(
            onDismissRequest = { showMemberLanguageDialog = null },
            title = {
                Text(
                    text = "${strings.tabLanguage}: $memberDisplayName",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AVAILABLE_LANGUAGES.forEach { lang ->
                        val isLangSelected = uiState.familyMemberLanguages[member] == lang.code
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    viewModel.setFamilyMemberLanguage(member, lang.code)
                                    showMemberLanguageDialog = null
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(lang.flagEmoji, fontSize = 20.sp)
                                Text(
                                    text = lang.nativeName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isLangSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                            if (isLangSelected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMemberLanguageDialog = null }) {
                    Text(strings.close)
                }
            }
        )
    }
}

