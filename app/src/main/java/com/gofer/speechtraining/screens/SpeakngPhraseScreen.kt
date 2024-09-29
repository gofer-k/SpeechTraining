package com.gofer.speechtraining.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.src.main.model.Phrase
import com.gofer.speechtraining.ui.theme.Purple40

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakingPhraseScreen(phrase: Phrase, navController: NavHostController) {
  val initialText = remember { mutableStateOf(true) }
  val speechText = remember { mutableStateOf("Your speech will appear here.") }
  val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    if (it.resultCode == Activity.RESULT_OK) {
      val data = it.data
      val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
      speechText.value = result?.get(0) ?: "No speech detected."
      initialText.value = false

      // EXTRA_CONFIDENCE_SCORES
    } else {
      speechText.value = "[Speech recognition failed.]"
    }
  }


  val originPhrase = phrase.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(phrase.language) else it.toString() }
  val withoutSuffixedOriginText = filterSuffixCharacters(originPhrase, listOf('.', ',', '?', '!'))

  // Scaffold floating button height
  var topBarHeight by remember { mutableIntStateOf(0) }
  val textSize = 20.sp

  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier.onGloballyPositioned { topBarHeight = it.size.height },
        title = {
          Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
              text = stringResource(id = TrainingScreenLabel.TrainingSpeakingPhrase.title),
              color = Color.White)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple40)
      )
    },
    bottomBar = {
      BottomAppBar {
        NavigationBarItem(
          selected = false,
          onClick = {
            // To prevent cyclist between previous and the current view
            navController.popBackStack()
          },
          icon = { Icon(imageVector = Icons.Filled.Close, contentDescription = null) })
      }
    }
  ){contentPadding ->
    Column(
      modifier = Modifier.fillMaxSize().padding(contentPadding),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(modifier = Modifier.padding(vertical = 20.dp),
        text = phrase.name, color = Color.Gray, fontSize = textSize)
      Button(modifier = Modifier.padding(vertical = 80.dp), onClick = {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
          RecognizerIntent.EXTRA_LANGUAGE_MODEL,
          RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, phrase.language)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Go on then, say something.")

        // REQUEST_WORD_CONFIDENCE"

        launcher.launch(intent)
      }) {
        Text("Start speech recognition")
      }
      Spacer(modifier = Modifier.padding(16.dp))
      Text(speechText.value,
        fontSize = textSize,
        color =
          if (initialText.value
            || withoutSuffixedOriginText.equals(speechText.value)) {
          Color.Green
        } else Color.Red)
    }
  }
}

fun filterSuffixCharacters(word: String, suffixes: List<Char>): String {
    return word.filterNot { char -> suffixes.any { char == it } }
}

@Composable
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun SpeakingPhraseScreen() {
  val navController = rememberNavController()
  SpeakingPhraseScreen(phrase = Phrase(name = "Sample"), navController = navController)
}