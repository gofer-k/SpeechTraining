package com.gofer.speechtraining.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gofer.speechtraining.R
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.src.main.model.Phrase
import com.gofer.speechtraining.ui.theme.PhraseString
import com.gofer.speechtraining.ui.theme.Purple40

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakingPhraseScreen(phrase: Phrase, navController: NavHostController) {
  val initialText = remember { mutableStateOf(true) }
  val speechDefaultText = remember { mutableStateOf("Your speech will appear here.") }
  val speechText = speechDefaultText
  val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    if (it.resultCode == Activity.RESULT_OK) {
      val data = it.data
      val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
      speechText.value = result?.get(0) ?: "No speech detected."
      initialText.value = false
    } else {
      speechText.value = "[Speech recognition failed.]"
    }
  }

  val withoutSuffixedOriginText = filterSuffixCharacters(phrase.name.trim().lowercase(), listOf('.', ',', '?', '!'))

  // Scaffold floating button height
  var topBarHeight by remember { mutableIntStateOf(0) }
  val textSize = 20.sp

  val defaultTextLabel = stringResource(id = TrainingScreenLabel.TrainingSpeakDefaultText.title)

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
      modifier = Modifier
        .fillMaxSize()
        .padding(contentPadding),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        modifier = Modifier.padding(top = 40.dp),
        text = phrase.name, color = PhraseString, fontSize = textSize)
      val ctx = LocalContext.current
      Button(onClick = {
        val url = Uri.parse(
          "https://www.diki.pl/slownik-angielskiego?q=" + Uri.encode(phrase.name))
        val c = CustomTabsIntent.Builder().build()
        c.launchUrl(ctx, url)
      }) {
        Row(verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Icon(
            modifier = Modifier.size(ButtonDefaults.IconSize),
            painter = painterResource(id = R.drawable.icon_baseline_description),
            contentDescription = null)
          Text(text = "Open description")
        }
      }
      Button(modifier = Modifier.padding(top = 80.dp, bottom = 12.dp), onClick = {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
          RecognizerIntent.EXTRA_LANGUAGE_MODEL,
          RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, phrase.language)
        intent.putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, phrase.language)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, phrase.language)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, defaultTextLabel)
        intent.putExtra(RecognizerIntent.EXTRA_ENABLE_LANGUAGE_SWITCH, phrase.language)

        launcher.launch(intent)
      }) {
        Text("Start speech recognition")
      }
      Text(
        text = if (initialText.value) speechDefaultText.value else speechText.value,
        fontSize = textSize,
        color =
          if (initialText.value
            || withoutSuffixedOriginText.equals(speechText.value.lowercase())) {
          Color.Green
        } else Color.Red)
    }
  }
}

fun filterSuffixCharacters(word: String, suffixes: List<Char>): String {
    return word.trim().filterNot { char -> suffixes.any { char == it } }
}

@Composable
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
fun SpeakingPhraseScreen() {
  val navController = rememberNavController()
  SpeakingPhraseScreen(phrase = Phrase(name = "Sample"), navController = navController)
}