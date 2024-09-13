package com.gofer.speechtraining

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PermissionsDialog( content: @Composable (requestedPermission: Boolean) -> Unit) {
  val activity = LocalContext.current as Activity

  val isMicrophonePermissionGranted = remember { mutableStateOf(false) }

  val microphonePermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission(),
    onResult = { isGranted ->
      isMicrophonePermissionGranted.value = isGranted
    }
  )

  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
  ) {
    Button(
      onClick = {
        microphonePermissionLauncher.launch(NeededPermission.RECORD_AUDIO.permission)
      }
    ) {
      Text(text = "Request record audio Permission")
    }
  }

  when {
    isMicrophonePermissionGranted.value -> {
      PermissionAlertDialog(
        onDismiss = { isMicrophonePermissionGranted.value = false },
        onOkClick = { isMicrophonePermissionGranted.value = false },
        onGoToAppSettingsClick = { activity.goToAppSetting()
          isMicrophonePermissionGranted.value = false
                                 },
        isPermissionDeclined = !activity.shouldShowRequestPermissionRationale(NeededPermission.RECORD_AUDIO.permission),
        neededPermission = NeededPermission.RECORD_AUDIO
      )
    }
  }

  content(isMicrophonePermissionGranted.value)
}

fun Activity.goToAppSetting() {
  val i =  Intent(
    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
    Uri.fromParts("package", packageName, null)
  )
  startActivity(i)
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PermissionsDialogPreview() {
  PermissionsDialog {
  }
}