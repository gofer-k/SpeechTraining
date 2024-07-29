package com.gofer.speechtraining.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.src.main.model.Phrase
import com.gofer.speechtraining.ui.theme.Pink40
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingScreen(
  trainingTopicName: String,
  phrases: List<Phrase>
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
      Text(text = (phrases.firstOrNull()?.name ?: "empty phrase"))
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