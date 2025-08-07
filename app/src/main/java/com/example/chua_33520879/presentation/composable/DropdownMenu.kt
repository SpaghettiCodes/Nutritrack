package com.example.chua_33520879.presentation.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(options: List<String>, state: State<String>, onClick: (String) -> Unit) {
    var dropDownIsOpen by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = dropDownIsOpen,
        onExpandedChange = { dropDownIsOpen = it },
    ) {
        OutlinedTextField(
            value = state.value,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Select Option") },
            label = {
                Text(
                    "My ID (Provided by your Clinician)",
                    fontWeight = FontWeight.Companion.Bold,
                    fontSize = 15.sp
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropDownIsOpen)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Companion.Gray,
                unfocusedBorderColor = Color.Companion.Gray,
                focusedContainerColor = Color.Companion.Transparent,
                unfocusedContainerColor = Color.Companion.Transparent
            ),
            modifier = Modifier.Companion
                .menuAnchor()
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp)
        )

        ExposedDropdownMenu(
            expanded = dropDownIsOpen,
            onDismissRequest = { dropDownIsOpen = false },
            shape = androidx.compose.foundation.shape.RoundedCornerShape(25.dp)
        ) {
            options.forEach { value ->
                DropdownMenuItem(
                    text = {
                        Text(value)
                    },
                    onClick = {
                        onClick(value)
                        dropDownIsOpen = false
                    }
                )
            }
        }
    }
}