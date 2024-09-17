package com.gofer.speechtraining

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage


@Composable
fun ImagePicker() {
  var imageUri by remember { mutableStateOf<Uri?>(null) }
  var imagePath by remember { mutableStateOf<String?>(null) }

  val context = LocalContext.current

  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    imageUri = uri
    imagePath = uri?.let { getRealPathFromURI(context, it) }
  }

  Column {
    Button(onClick = { launcher.launch("image/*") }) {
      Text("Select Image")
    }

    imageUri?.let {
      AsyncImage(
        model = it,
        contentDescription = "Selected image",
        modifier = Modifier.fillMaxWidth()
      )
      Text("Image path: $imagePath")
    }
  }
}

// Helper function to convert Uri to file path (implementation may vary)
fun getRealPathFromURI(context: Context, contentUri: Uri): String? = contentUri.path