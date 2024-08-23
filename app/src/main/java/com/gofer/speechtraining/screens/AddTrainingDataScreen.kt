package com.gofer.speechtraining.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddTrainingDataScreen(navController: NavController) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(text = "Add training phrase")/*TODO*/ })},
    bottomBar = {
      BottomAppBar() {
        NavigationBarItem(
          label = { Text(text = TrainingScreenLabel.TrainingCancel.name) },
          selected = false,
          onClick = { /*TODO*/ },
          icon = { /*TODO*/ })
        NavigationBarItem(
          label = { Text(text = TrainingScreenLabel.TrainingSave.name) },
          selected = false,
          onClick = { /*TODO*/ },
          icon = { /*TODO*/ })
      }
    }
  ) { contentPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues = contentPadding)) {
      Spacer(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 24.dp))
      Icon(
        imageVector = Icons.Filled.Build,
        contentDescription = null,
        modifier = Modifier
          .fillMaxWidth()
          .scale(2.0f))
      Spacer(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 48.dp))
      TextField(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 4.dp),
        value = "Phrase text",
        shape = RoundedCornerShape(24.dp),
        onValueChange = {})
      Spacer(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 48.dp))
      LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 48.dp)) {
        items(listOf("English", "Polish")) {
          Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = it, fontSize = 20.sp)
        }
      }
    }
  }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AddTrainingDataScreenPreview() {
  SpeechTrainingTheme {
    val navController = rememberNavController()
    AddTrainingDataScreen(navController = navController)
  }
}