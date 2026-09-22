package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.oracle.CloudConnectionState
import com.example.ui.theme.*

@Composable
fun OracleCloudStatusBadge(
    connectionState: CloudConnectionState,
    isOfflineMode: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val (bgColor, dotColor, label) = when {
        isOfflineMode -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            StatusWarning,
            "Offline Cache Mode"
        )
        connectionState == CloudConnectionState.SYNCING -> Triple(
            OracleTertiaryContainerLight.copy(alpha = 0.5f),
            OracleTertiaryLight,
            "Syncing 26AI..."
        )
        connectionState == CloudConnectionState.CONNECTED -> Triple(
            EmeraldPrimaryContainerLight.copy(alpha = 0.45f),
            StatusGood,
            "Oracle 26AI Synced"
        )
        else -> Triple(
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
            StatusCritical,
            "Sync Error"
        )
    }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag("oracle_status_badge"),
        color = bgColor,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor.copy(alpha = if (connectionState == CloudConnectionState.SYNCING) pulseAlpha else 1f))
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun FamilyMemberAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Int = 28,
    avatarEmoji: String? = null,
    colorHex: String? = null
) {
    val fallbackColorPair = when (name.lowercase()) {
        "mom" -> Color(0xFFE9D5FF) to Color(0xFF6B21A8)
        "dad" -> Color(0xFFBAE6FD) to Color(0xFF0369A1)
        "maya" -> Color(0xFFFBCFE8) to Color(0xFF9D174D)
        "alex" -> Color(0xFFFED7AA) to Color(0xFFC2410C)
        else -> Color(0xFFBBF7D0) to Color(0xFF15803D)
    }

    val (bgColor, textColor) = if (!colorHex.isNullOrBlank()) {
        try {
            val c = Color(android.graphics.Color.parseColor(colorHex))
            c.copy(alpha = 0.25f) to c
        } catch (e: Exception) {
            fallbackColorPair
        }
    } else {
        fallbackColorPair
    }

    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        if (!avatarEmoji.isNullOrBlank() && avatarEmoji != "👤") {
            Text(
                text = avatarEmoji,
                fontSize = (size * 0.55).sp
            )
        } else {
            Text(
                text = name.take(1).uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = (size * 0.45).sp
                ),
                color = textColor
            )
        }
    }
}

@Composable
fun UrgencyBadge(
    urgency: String,
    modifier: Modifier = Modifier
) {
    val (label, containerColor, textColor) = when (urgency) {
        "CRITICAL_OUT_OF_STOCK" -> Triple("Out of Stock", StatusCritical.copy(alpha = 0.15f), StatusCritical)
        "EXPIRING_SOON" -> Triple("Expiring Soon", StatusWarning.copy(alpha = 0.18f), AmberSecondaryLight)
        else -> Triple("Low Stock", AmberSecondaryContainerLight, AmberOnSecondaryContainerLight)
    }

    Surface(
        modifier = modifier,
        color = containerColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            ),
            color = textColor
        )
    }
}

@Composable
fun StatMetricCard(
    title: String,
    value: String,
    subtitle: String,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = iconColor
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}
