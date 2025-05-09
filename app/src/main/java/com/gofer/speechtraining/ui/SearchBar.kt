package com.gofer.speechtraining.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gofer.speechtraining.ui.theme.BlueTrainingTopBarr

@Composable
fun SearchBar(
    onQueryChanged: ((String) -> Unit)? = null,
    onSearchDone: ((Boolean) -> Unit)? = null,
    backgroundColor: Color
) {
    // State variable to hold the search query
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        val keyBoardController = LocalSoftwareKeyboardController.current

        TextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                disabledContainerColor = backgroundColor,
                focusedTextColor = Color.White),
            value = searchQuery, // The current value of the text field
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                // The action property type have to consistent with keyboardActions type
                imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyBoardController?.hide()
                    onSearchDone?.invoke(true)
                }
            ),
            onValueChange = { newValue -> // Called when the text changes
                searchQuery = newValue // Update the state with the new text
                if (searchQuery.isNotBlank()) {
                    onQueryChanged?.invoke(newValue)
                }
            },
            label = { Text("Search") }, // Optional label
            modifier = Modifier.fillMaxWidth() // Make the text field fill the width
        )
    }
}

@Composable
@Preview(showBackground = true)
fun BasicSearchBarPreview() {
    SearchBar(backgroundColor = BlueTrainingTopBarr)
}