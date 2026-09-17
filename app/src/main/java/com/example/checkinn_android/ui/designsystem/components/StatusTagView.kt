package com.example.checkinn_android.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.checkinn_android.domain.model.BookingStatusType
import com.example.checkinn_android.ui.designsystem.DS
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography

val BookingStatusType.icon: ImageVector
    get() = when (this) {
        BookingStatusType.Initiated -> Icons.Default.Schedule
        BookingStatusType.Rejected -> Icons.Default.Close
        BookingStatusType.Approved -> Icons.AutoMirrored.Filled.ExitToApp
        BookingStatusType.OnHold -> Icons.Default.PauseCircle
    }

val BookingStatusType.color: Color
    @Composable get() = when (this) {
        BookingStatusType.Initiated -> DSColors.amber
        BookingStatusType.Rejected -> DSColors.danger
        BookingStatusType.Approved -> DSColors.success
        BookingStatusType.OnHold -> DSColors.appTint
    }

val BookingStatusType.accessibilityLabel: String
    get() = when (this) {
        BookingStatusType.Initiated -> "Initiated"
        BookingStatusType.Rejected -> "Checkin Rejected"
        BookingStatusType.Approved -> "Checkin Approved"
        BookingStatusType.OnHold -> "Check on Hold"
    }

@Composable
fun StatusTagView(
    status: BookingStatusType,
    modifier: Modifier = Modifier,
    size: Dp = DS.Icon.lg
) {
    Box(
        modifier = modifier
            .size(size)
            .background(
                color = status.color.copy(alpha = 0.15f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = status.icon,
            contentDescription = status.accessibilityLabel,
            tint = status.color,
            modifier = Modifier.size(size * 0.7f)
        )
    }
}

@Composable
fun StatusBadge(
    status: BookingStatusType,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = status.color.copy(alpha = 0.15f),
                shape = RoundedCornerShape(percent = 50)
            )
            .padding(horizontal = DS.Spacing.sm, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatusTagView(status = status, size = DS.Icon.sm)
        Spacer(modifier = Modifier.width(DS.Spacing.xs))
        Text(
            text = status.title,
            style = DSTypography.caption.copy(fontWeight = FontWeight.Bold),
            color = status.color,
            maxLines = 1
        )
    }
}
