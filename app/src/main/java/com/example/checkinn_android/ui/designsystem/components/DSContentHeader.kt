package com.example.checkinn_android.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.checkinn_android.ui.designsystem.DS
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography

enum class DSContentHeaderStyle {
    Hero,
    Title,
    Section
}

enum class DSIconBadgeSize(
    val iconSize: Dp,
    val frameSize: Dp,
    val radius: Dp
) {
    Regular(
        iconSize = DS.Icon.md,
        frameSize = DS.Layout.minimumTapTarget,
        radius = DS.Radius.md
    ),
    Large(
        iconSize = DS.Icon.xl,
        frameSize = 56.dp,
        radius = DS.Radius.lg
    )
}

@Composable
fun DSIconBadge(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = DSColors.appTint,
    size: DSIconBadgeSize = DSIconBadgeSize.Regular
) {
    Box(
        modifier = modifier
            .size(size.frameSize)
            .background(
                color = tint.copy(alpha = 0.12f),
                shape = RoundedCornerShape(size.radius)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(size.iconSize)
        )
    }
}

@Composable
fun DSContentHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
    style: DSContentHeaderStyle = DSContentHeaderStyle.Title
) {
    val titleStyle = when (style) {
        DSContentHeaderStyle.Hero -> DSTypography.hero
        DSContentHeaderStyle.Title -> DSTypography.title
        DSContentHeaderStyle.Section -> DSTypography.sectionTitle
    }

    val subtitleStyle = when (style) {
        DSContentHeaderStyle.Hero, DSContentHeaderStyle.Title -> DSTypography.callout
        DSContentHeaderStyle.Section -> DSTypography.caption
    }

    val subtitleColor = when (style) {
        DSContentHeaderStyle.Hero, DSContentHeaderStyle.Title -> DSColors.secondaryText
        DSContentHeaderStyle.Section -> DSColors.tertiaryText
    }

    Column(modifier = modifier.fillMaxWidth()) {
        if (icon != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                DSIconBadge(icon = icon, size = DSIconBadgeSize.Large)
                Spacer(modifier = Modifier.width(DS.Spacing.sm))
                Text(
                    text = title,
                    style = titleStyle,
                    color = DSColors.primaryText
                )
            }
        } else {
            Text(
                text = title,
                style = titleStyle,
                color = DSColors.primaryText
            )
        }

        if (!subtitle.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(DS.Spacing.xxs))
            Text(
                text = subtitle,
                style = subtitleStyle,
                color = subtitleColor
            )
        }
    }
}
