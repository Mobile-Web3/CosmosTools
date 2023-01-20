package com.mobileweb3.cosmostools.android.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AmountTextField(
    title: String,
    text: String,
    readOnly: Boolean = false,
    onTextChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp
            ),
        value = text,
        label = { Text(title) },
        trailingIcon = null,
        onValueChange = {
            val newText = if (it.isEmpty()){
                it
            } else {
                when (it.toDoubleOrNull()) {
                    null -> text //old value
                    else -> it   //new value
                }
            }
            onTextChanged.invoke(newText)
        },
        readOnly = readOnly,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        )
    )
}