package com.gofer.speechtraining

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gofer.speechtraining.src.main.model.Phrase
import com.gofer.speechtraining.ui.theme.Pink80
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingConfigurationScreen(
  phrases: List<Phrase>,
  navController: NavController,
  trainingTopicName: String
) {
  val trainingPhrasesState = remember { phrases.toMutableStateList() }

  Scaffold(
    topBar = {
      TopAppBar(title = { 
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
          Text(text = TrainingScreenLabel.TrainingContents.name.plus(trainingTopicName))
        }
      },
      colors = topAppBarColors(containerColor = Pink80)
      )
    }
  ) {
    LazyColumn(modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Top,) {
      itemsIndexed(trainingPhrasesState) { index, phrase ->
        if (index != 0) Spacer(Modifier.height(2.dp))
        var selected by remember { mutableStateOf(false) }
        Box(modifier = Modifier
          .shadow(4.dp, shape = MaterialTheme.shapes.small)
          .fillMaxSize()
          .clip(MaterialTheme.shapes.small)
          .background(if (selected) Color("#9233eb".toColorInt()) else MaterialTheme.colorScheme.surface)
          .padding(16.dp)
          .clickable {
            selected = selected.not()
            phrase.toggle()
////            navController.navigate( yo navigable to training screen)
          },
          contentAlignment = Alignment.Center
        ) {
          Text( text = phrase.name, color = if (selected) Color.White else Color.Black)
        }
      }
    }
  }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
private fun TrainingContentsScreenPreview() {
  SpeechTrainingTheme {
    val navController = rememberNavController()
    TrainingConfigurationScreen(listOf(), navController = navController, "Topic")
  }
}