package com.gofer.speechtraining.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.ui.theme.Pink40
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(
  trainingTopicName: String,
  phrases: List<String>
) {
  Scaffold(
    topBar = {
      TopAppBar(title = {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
          Text(text = TrainingScreenLabel.TrainingConfiguration.name.plus(trainingTopicName))
        }
      },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Pink40)
      )
    }
  ) {
    LazyColumn(modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Top,) {
      items(phrases) { phrase ->
        Box(modifier = Modifier
          .shadow(4.dp, shape = MaterialTheme.shapes.small)
          .fillMaxSize()
          .clip(MaterialTheme.shapes.small)
          .padding(16.dp)
          .clickable {
          },
          contentAlignment = Alignment.Center
        ) {
          Text( text = phrase)
        }
      }
    }
  }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
private fun TrainingScreenPreview() {
  SpeechTrainingTheme {
    val navController = rememberNavController()
    TrainingScreen("Topic", listOf())
  }
}