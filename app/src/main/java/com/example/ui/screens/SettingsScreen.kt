package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.ui.viewmodel.PantryUiState
import com.example.ui.viewmodel.PantryViewModel

@Composable
fun SettingsScreen(
    uiState: PantryUiState,
    viewModel: PantryViewModel,
    modifier: Modifier = Modifier
) {
    // General Settings directly hosts Preferred Language Communications & Regional Settings
    LanguageSettingsScreen(
        uiState = uiState,
        viewModel = viewModel,
        modifier = modifier.testTag("general_settings_screen")
    )
}

