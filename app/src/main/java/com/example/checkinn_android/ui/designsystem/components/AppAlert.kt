package com.example.checkinn_android.ui.designsystem.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography

data class AppAlert(
    val title: String = "Alert",
    val message: String,
    val buttonTitle: String = "OK",
    val onDismiss: (() -> Unit)? = null
)

@Composable
fun DSAlertDialog(
    alert: AppAlert?,
    onDismissRequest: () -> Unit
) {
    if (alert != null) {
        AlertDialog(
            onDismissRequest = {
                alert.onDismiss?.invoke()
                onDismissRequest()
            },
            title = {
                Text(
                    text = alert.title,
                    style = DSTypography.title,
                    color = DSColors.primaryText
                )
            },
            text = {
                Text(
                    text = alert.message,
                    style = DSTypography.body,
                    color = DSColors.secondaryText
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        alert.onDismiss?.invoke()
                        onDismissRequest()
                    }
                ) {
                    Text(
                        text = alert.buttonTitle,
                        style = DSTypography.bodyEmphasized,
                        color = DSColors.appTint
                    )
                }
            },
            containerColor = DSColors.surface
        )
    }
}
