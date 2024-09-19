package com.gofer.speechtraining.src.main.model

import android.net.Uri

data class Topic(
  val id: Long = 0L,
  val name: String = "Add training topic",
  val isSelected: Boolean = false,
  var imageUri: Uri? = null
)