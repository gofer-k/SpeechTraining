package com.gofer.speechtraining

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gofer.speechtraining.ui.theme.Pink80
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingContentsScreen(navController: NavController, trainingTopicName: String?) {
  val trainingItemsState = remember { TrainingItemsState() }
  // Todo: initial training items state list

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
      verticalArrangement = Arrangement.Top) {
//        itemsIndexed(trainingItemsState.trainingItems) { index,  ->}
      itemsIndexed(trainingItemsState.trainingItems) { index, String ->
        // Todo: fill - in training items.
      }

    }
  }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
private fun TrainingContentsScreenPreview() {
  SpeechTrainingTheme {
    val navController = rememberNavController()
    TrainingContentsScreen(navController = navController, "Topic")
  }
}