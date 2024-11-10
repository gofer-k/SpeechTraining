package com.gofer.speechtraining.ui

import android.Manifest
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsDialog( content: @Composable (requestedPermission: Boolean) -> Unit) {
  val recordAudioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
  var fabHeight by remember { mutableIntStateOf(0) }

  Surface(modifier = Modifier.onGloballyPositioned { fabHeight = it.size.height }) {
    if (recordAudioPermissionState.status.isGranted) {
      content(recordAudioPermissionState.status.isGranted)
    } else {
      Column(
        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)) {
        val textToShow = if (recordAudioPermissionState.status.shouldShowRationale) {
          // If the user has denied the permission but the rationale can be shown,
          // then gently explain why the app requires this permission
          "The record audio is important for this app. Please grant the permission."
        } else {
          // If it's the first time the user lands on this feature, or the user
          // doesn't want to be asked again for this permission, explain that the
          // permission is required
          "Record audio permission required for this feature to be available. " +
                  "Please grant the permission"
        }
        Text(modifier = Modifier.padding(vertical = 50.dp), text = textToShow, textAlign = TextAlign.Center)
        Button(modifier = Modifier.align(Alignment.CenterHorizontally).scale(1.2f), onClick = { recordAudioPermissionState.launchPermissionRequest() }) {
          Text("Request permission")
        }
      }
    }
  }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PermissionsDialogPreview() {
  PermissionsDialog {
  }
}