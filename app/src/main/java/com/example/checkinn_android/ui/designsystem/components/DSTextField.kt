package com.example.checkinn_android.ui.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.checkinn_android.ui.designsystem.DS
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography

@Composable
fun DSTextField(
    title: String,
    placeholder: String,
    leadingIcon: ImageVector,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    errorMessage: String? = null,
    isSecure: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }
    var isSecureTextVisible by remember { mutableStateOf(false) }

    val hasError = !errorMessage.isNullOrEmpty()

    val borderColor = when {
        hasError -> DSColors.danger
        isFocused -> DSColors.appTint
        else -> DSColors.separator.copy(alpha = 0.55f)
    }

    val iconColor = if (hasError) DSColors.danger else DSColors.appTint

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = DSTypography.caption,
            color = DSColors.secondaryText
        )

        Spacer(modifier = Modifier.height(DS.Spacing.xs))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .background(
                    color = DSColors.surface,
                    shape = RoundedCornerShape(DS.Radius.lg)
                )
                .border(
                    width = DS.Stroke.thin,
                    color = borderColor,
                    shape = RoundedCornerShape(DS.Radius.lg)
                )
                .padding(
                    start = DS.Spacing.md,
                    end = if (isSecure) DS.Spacing.xxs else DS.Spacing.md
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(DS.Icon.sm)
            )

            Spacer(modifier = Modifier.width(DS.Spacing.sm))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = DS.Spacing.sm)
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = DSTypography.body,
                        color = DSColors.secondaryText.copy(alpha = 0.6f)
                    )
                }

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isFocused = it.isFocused },
                    textStyle = DSTypography.body.copy(color = DSColors.primaryText),
                    cursorBrush = SolidColor(DSColors.appTint),
                    singleLine = true,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    visualTransformation = if (isSecure && !isSecureTextVisible) {
                        PasswordVisualTransformation()
                    } else {
                        VisualTransformation.None
                    }
                )
            }

            if (isSecure) {
                IconButton(
                    onClick = { isSecureTextVisible = !isSecureTextVisible },
                    modifier = Modifier.size(DS.Layout.minimumTapTarget)
                ) {
                    Icon(
                        imageVector = if (isSecureTextVisible) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = if (isSecureTextVisible) "Hide password" else "Show password",
                        tint = DSColors.secondaryText,
                        modifier = Modifier.size(DS.Icon.sm)
                    )
                }
            }
        }

        if (hasError) {
            Spacer(modifier = Modifier.height(DS.Spacing.xs))
            Text(
                text = errorMessage,
                style = DSTypography.footnote,
                color = DSColors.danger
            )
        }
    }
}
