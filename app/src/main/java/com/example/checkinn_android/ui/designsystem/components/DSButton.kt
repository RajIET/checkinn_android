package com.example.checkinn_android.ui.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.checkinn_android.ui.designsystem.DS
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography

enum class DSButtonVariant {
    Primary,
    Secondary,
    Destructive,
    Ghost
}

enum class DSButtonSize {
    Compact,
    Regular;

    val verticalPadding: Dp
        get() = when (this) {
            Compact -> DS.Spacing.xs
            Regular -> DS.Spacing.sm
        }

    val horizontalPadding: Dp
        get() = when (this) {
            Compact -> DS.Spacing.sm
            Regular -> DS.Spacing.md
        }
}

enum class DSIconDirection {
    Left,
    Right
}

@Composable
fun DSButton(
    title: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    variant: DSButtonVariant = DSButtonVariant.Primary,
    size: DSButtonSize = DSButtonSize.Regular,
    isLoading: Boolean = false,
    isDisabled: Boolean = false,
    iconDirection: DSIconDirection = DSIconDirection.Right,
    action: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        label = "ds_button_scale"
    )

    val contentColor = when (variant) {
        DSButtonVariant.Primary, DSButtonVariant.Destructive -> Color.White
        DSButtonVariant.Secondary, DSButtonVariant.Ghost -> DSColors.primaryText
    }

    val baseBackgroundColor = when (variant) {
        DSButtonVariant.Primary -> DSColors.cyan
        DSButtonVariant.Secondary -> DSColors.surface
        DSButtonVariant.Destructive -> DSColors.danger
        DSButtonVariant.Ghost -> if (isPressed) DSColors.fill else Color.Transparent
    }

    val backgroundColor = if (isPressed && variant != DSButtonVariant.Ghost) {
        baseBackgroundColor.copy(alpha = 0.82f)
    } else {
        baseBackgroundColor
    }

    val border = if (variant == DSButtonVariant.Secondary) {
        BorderStroke(DS.Stroke.thin, DSColors.separator)
    } else {
        null
    }

    val buttonAlpha = if (isDisabled) 0.55f else 1f

    Button(
        onClick = action,
        enabled = !isDisabled && !isLoading,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = DS.Layout.minimumTapTarget)
            .scale(scale)
            .alpha(buttonAlpha),
        shape = RoundedCornerShape(DS.Radius.md),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = backgroundColor,
            disabledContentColor = contentColor
        ),
        border = border,
        contentPadding = PaddingValues(
            horizontal = size.horizontalPadding,
            vertical = size.verticalPadding
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(DS.Icon.sm),
                    color = contentColor,
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (icon != null && iconDirection == DSIconDirection.Left) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(DS.Icon.sm),
                            tint = contentColor
                        )
                        Spacer(modifier = Modifier.width(DS.Spacing.xs))
                    }

                    Text(
                        text = title,
                        style = DSTypography.bodyEmphasized,
                        color = contentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (icon != null && iconDirection == DSIconDirection.Right) {
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(DS.Icon.sm),
                            tint = contentColor
                        )
                    }
                }
            }
        }
    }
}
