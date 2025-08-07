package com.example.chua_33520879.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Eye
import com.composables.icons.lucide.EyeOff
import com.composables.icons.lucide.Lucide

@Composable
fun PasswordField(
    value: String,
    label: String,
    placeholder: String,
    setValue: (String) -> Unit
) {
    var passwordHidden by remember { mutableStateOf(true) }

    OutlinedTextField(
        value = value,
        onValueChange = { setValue(it) },
        label = {
            Text(
                label,
                fontWeight = FontWeight.Companion.Bold,
                fontSize = 15.sp
            )
        },
        placeholder = {
            Text(placeholder)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Companion.Gray,
            unfocusedBorderColor = Color.Companion.Gray,
            focusedContainerColor = Color.Companion.Transparent,
            unfocusedContainerColor = Color.Companion.Transparent
        ),
        visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (passwordHidden) {
                Icon(Lucide.EyeOff, null, modifier = Modifier.clickable(onClick = { passwordHidden = false }))
            } else {
                Icon(Lucide.Eye, null, modifier = Modifier.clickable(onClick = { passwordHidden = true }))
            }
        },
        singleLine = true,
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = RoundedCornerShape(15.dp)
    )
}