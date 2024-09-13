package com.gofer.speechtraining

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionAlertDialog(
  neededPermission: NeededPermission,
  isPermissionDeclined: Boolean,
  onDismiss: () -> Unit,
  onOkClick: () -> Unit,
  onGoToAppSettingsClick: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(text = neededPermission.title) },
    text = { Text(text = neededPermission.permissionTextProvider(isPermissionDeclined))},
    confirmButton = {
      TextButton( onClick = {
        if (isPermissionDeclined)
          onGoToAppSettingsClick()
        else
          onOkClick()
        onDismiss()
      },
        content = {Text("On") }
      )
    },
    dismissButton = {
      TextButton( onClick = { onDismiss() }, content = {
        Text("Dismiss")
      })
    }
  )
}
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PermissionAlertDialogPreview() {
  PermissionAlertDialog(
    neededPermission = NeededPermission.RECORD_AUDIO,
    isPermissionDeclined = true,
    onDismiss=  {},
    onOkClick = {},
    onGoToAppSettingsClick = {}
  )
}
