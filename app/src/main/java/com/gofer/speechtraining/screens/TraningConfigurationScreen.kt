package com.gofer.speechtraining.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gofer.speechtraining.PhraseState
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.getTrainingSpeakIcon
import com.gofer.speechtraining.src.main.model.Phrase
import com.gofer.speechtraining.src.main.model.TtsViewModel
import com.gofer.speechtraining.ui.theme.Pink80
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingConfigurationScreen(
  phrases: List<Phrase>,
  navController: NavController,
  trainingTopicName: String,
  ttsViewModel: TtsViewModel = TtsViewModel()
) {
//  val trainingPhrasesState = remember { phrases.toMutableStateList() }
  val context = LocalContext.current
  val phraseListState = remember { PhraseState() }
  phraseListState.setPhraseList(phrases)

  Scaffold(
    topBar = {
      TopAppBar(title = { 
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
          Text(text = TrainingScreenLabel.TrainingConfiguration.name.plus(trainingTopicName))
        }
      },
      colors = topAppBarColors(containerColor = Pink80)
      )
    },
    floatingActionButton = {
      FloatingActionButton(onClick = {}) {
        IconButton(onClick = { navController.navigate(TrainingScreenLabel.TrainingAddPhrase.name) }) {
          Icon(imageVector = Icons.Filled.Add, contentDescription = null )
        }
      }
    }
  ) { contentPadding ->
    Column(modifier = Modifier
      .fillMaxSize()
      .padding(contentPadding)) {
      Spacer(Modifier.height(2.dp))
      LazyColumn(modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,) {
        itemsIndexed(phraseListState.phraseList) { index, phrase ->
          Box(modifier = Modifier
            .shadow(1.dp, shape = MaterialTheme.shapes.extraSmall)
            .clip(MaterialTheme.shapes.extraSmall)
           ,contentAlignment = Alignment.CenterStart
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween) {
              Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = phrase.name,
                style = TextStyle(textIndent = TextIndent(firstLine = 8.sp)),
                fontSize = 20.sp,
                fontWeight = if (phrase.isSelected) FontWeight.Bold else FontWeight.Normal
              )
              IconButton(onClick = {
                ttsViewModel.onSpeakTrainingPhrase(phrase, context,
                  {
                    phrase.toggle()
                    phraseListState.onSelectedPhrase(phrase)
                  })
              }) {
                Icon(
                  painterResource(id = getTrainingSpeakIcon(isSystemInDarkTheme())),
                  contentDescription = TrainingScreenLabel.TrainingPhraseSpeech.name)
              }
            }
          }
        }
      }
    }
  }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TrainingContentsScreenPreview() {
  SpeechTrainingTheme {
    val navController = rememberNavController()
    TrainingConfigurationScreen(listOf(Phrase("one"), Phrase("two"), Phrase("three")), navController = navController, "Topic")
  }
}