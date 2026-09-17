package com.example.checkinn_android.ui.designsystem.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun DSUsernameField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Username",
    placeholder: String = "Enter your username",
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    DSTextField(
        title = title,
        placeholder = placeholder,
        leadingIcon = Icons.Default.Person,
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        errorMessage = errorMessage,
        isSecure = false
    )
}
