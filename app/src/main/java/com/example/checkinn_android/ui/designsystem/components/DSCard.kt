package com.example.checkinn_android.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.checkinn_android.ui.designsystem.DS
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography

@Composable
fun DSCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = DS.Stroke.hairline,
                color = DSColors.separator.copy(alpha = 0.5f),
                shape = RoundedCornerShape(DS.Radius.lg)
            ),
        shape = RoundedCornerShape(DS.Radius.lg),
        colors = CardDefaults.cardColors(
            containerColor = DSColors.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Box(modifier = Modifier.padding(DS.Spacing.md)) {
            content()
        }
    }
}

@Composable
fun DSMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = DSColors.appTint
) {
    DSCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(DS.Layout.minimumTapTarget)
                    .background(
                        color = tint.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(DS.Radius.md)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(DS.Icon.md)
                )
            }

            Spacer(modifier = Modifier.width(DS.Spacing.md))

            Column {
                Text(
                    text = value,
                    style = DSTypography.title,
                    color = DSColors.primaryText
                )
                Text(
                    text = title,
                    style = DSTypography.caption,
                    color = DSColors.secondaryText
                )
            }
        }
    }
}
