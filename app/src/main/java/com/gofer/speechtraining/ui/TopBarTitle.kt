package com.gofer.speechtraining.ui

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.gofer.speechtraining.TrainingScreenLabel

@Composable
fun TopBarTitle(navController: NavController, title: TrainingScreenLabel, color: Color) {
  TopBarTitle(navController, title  = stringResource(id = title.title), color = color)
}

@Composable
fun TopBarTitle(navController: NavController, title: String, color: Color) {
  Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    BackButton(navController = navController, color = color)
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
      Text(text = title, color = color)
    }
  }
}

@Composable
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
fun TopBarTitlePreview() {
  TopBarTitle(navController = NavController(LocalContext.current),
    title = "Tittle", color = if(isSystemInDarkTheme()) Color.White else Color.Black)
}