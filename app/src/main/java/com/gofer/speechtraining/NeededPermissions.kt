package com.gofer.speechtraining

enum class NeededPermission( val permission: String,
                              val title: String,
                              val description: String,
                              val permanentlyDeniedDescription: String) {
  RECORD_AUDIO(
    permission = android.Manifest.permission.RECORD_AUDIO,
    title = "Record Audio permission",
    description = "This permission is needed to access your microphone. Please grant the permission.",
    permanentlyDeniedDescription = "This permission is needed to access your microphone. Please grant the permission in app settings.",
  );

  fun permissionTextProvider(isPermanentDenied: Boolean): String {
    return if (isPermanentDenied) this.permanentlyDeniedDescription else this.description
  }
}
