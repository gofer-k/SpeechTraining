package com.gofer.speechtraining.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun BackButton(navController: NavController, color: Color) {
  IconButton(onClick = {
    navController.popBackStack()
  }) {
    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = color)
  }
}