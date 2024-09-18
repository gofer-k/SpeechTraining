package com.gofer.speechtraining

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun ImagePicker(modifier: Modifier = Modifier, content: @Composable()(imageUri: Uri) -> Unit) {
  val defaultTopicIcon = getDefaultTopicIcon()
  var pickedImageUri by remember { mutableStateOf<Uri?>(null) }

  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    pickedImageUri = uri
  }

  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Button(modifier = modifier.padding(vertical = 4.dp), onClick = { launcher.launch("image/*") }) {
      Text(stringResource(id = TrainingScreenLabel.TrainingTopicImageCustomize.title))
    }
    pickedImageUri?.let {
      AsyncImage(
        model = pickedImageUri ?: stringResource(id = defaultTopicIcon),
        contentDescription = stringResource(id = TrainingScreenLabel.TrainingTopicImage.title),
        placeholder = painterResource(id = defaultTopicIcon),
        modifier = modifier.fillMaxWidth())
    } ?: Image(painter = painterResource(id = defaultTopicIcon), contentDescription = null)
  }
  content(pickedImageUri ?: Uri.parse(stringResource(id = defaultTopicIcon)))
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun  ImagePickerPreview() {
  ImagePicker {}
}