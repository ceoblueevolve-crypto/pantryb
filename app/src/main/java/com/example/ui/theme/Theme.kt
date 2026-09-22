package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
  primary = EmeraldPrimaryDark,
  onPrimary = EmeraldOnPrimaryDark,
  primaryContainer = EmeraldPrimaryContainerDark,
  onPrimaryContainer = EmeraldOnPrimaryContainerDark,
  secondary = AmberSecondaryDark,
  onSecondary = AmberOnSecondaryDark,
  secondaryContainer = AmberSecondaryContainerDark,
  onSecondaryContainer = AmberOnSecondaryContainerDark,
  tertiary = OracleTertiaryDark,
  onTertiary = OracleOnTertiaryDark,
  tertiaryContainer = OracleTertiaryContainerDark,
  onTertiaryContainer = OracleOnTertiaryContainerDark,
  background = BackgroundDark,
  onBackground = OnBackgroundDark,
  surface = SurfaceDark,
  onSurface = OnSurfaceDark,
  surfaceVariant = SurfaceVariantDark,
  onSurfaceVariant = OnSurfaceVariantDark,
)

private val LightColorScheme = lightColorScheme(
  primary = EmeraldPrimaryLight,
  onPrimary = EmeraldOnPrimaryLight,
  primaryContainer = EmeraldPrimaryContainerLight,
  onPrimaryContainer = EmeraldOnPrimaryContainerLight,
  secondary = AmberSecondaryLight,
  onSecondary = AmberOnSecondaryLight,
  secondaryContainer = AmberSecondaryContainerLight,
  onSecondaryContainer = AmberOnSecondaryContainerLight,
  tertiary = OracleTertiaryLight,
  onTertiary = OracleOnTertiaryLight,
  tertiaryContainer = OracleTertiaryContainerLight,
  onTertiaryContainer = OracleOnTertiaryContainerLight,
  background = BackgroundLight,
  onBackground = OnBackgroundLight,
  surface = SurfaceLight,
  onSurface = OnSurfaceLight,
  surfaceVariant = SurfaceVariantLight,
  onSurfaceVariant = OnSurfaceVariantLight,
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

