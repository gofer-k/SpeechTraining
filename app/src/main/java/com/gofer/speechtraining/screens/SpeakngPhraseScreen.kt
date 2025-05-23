package com.gofer.speechtraining.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.speech.RecognizerIntent
 import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.gofer.speechtraining.getTrainingRecordIcon
import com.gofer.speechtraining.getTrainingSpeakIcon
import com.gofer.speechtraining.src.main.model.Phrase
import com.gofer.speechtraining.src.main.model.TtsViewModel
import com.gofer.speechtraining.ui.TopBarTitle
import com.gofer.speechtraining.ui.theme.PhrasePronString
import com.gofer.speechtraining.ui.theme.PhrasePronStringDark
import com.gofer.speechtraining.ui.theme.PhraseString
import com.gofer.speechtraining.ui.theme.PhraseStringDark
import com.gofer.speechtraining.ui.theme.Purple40
import androidx.core.net.toUri

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakingPhraseScreen(phrase: Phrase, navController: NavHostController) {
  // Scaffold floating button height
  var topBarHeight by remember { mutableIntStateOf(0) }
  val textSize = 20.sp
  val destLang = when(phrase.language.language) {
    "en" -> "pl"
    "us" -> "pl"
    "pl" -> "us"
    else -> {
      Toast.makeText(LocalContext.current, "Unsupported language: ${phrase.language.language}", Toast.LENGTH_SHORT).show()
      return
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        modifier = Modifier.onGloballyPositioned { topBarHeight = it.size.height },
        title = {
          TopBarTitle(navController = navController,
            title = TrainingScreenLabel.TrainingSpeakingPhrase,
            color=  Color.White)
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
      val textColor = if (isSystemInDarkTheme()) PhraseStringDark else PhraseString
      val pronColor = if (isSystemInDarkTheme()) PhrasePronStringDark else PhrasePronString

      Text(
        modifier = Modifier.padding(top = 40.dp, bottom = 12.dp),
        text = phrase.name, color = textColor, fontSize = textSize)
      Text(
        modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
        text = phrase.pron, color = pronColor, fontSize = textSize)
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        val ctx = LocalContext.current
        val encodePhraseName = Uri.encode(phrase.name)
        val urlDiki =
            "https://www.diki.pl/slownik-angielskiego?q=${encodePhraseName}".toUri()
        LinkButton(ctx, "Diki", urlDiki)
        val urlTranslate =
            "https://translate.google.com/?sl=${phrase.language}&tl=${destLang}&text=${encodePhraseName}".toUri()
        Log.d("SpeakingPhraseScreen", "urlTranslate: $urlTranslate")
        LinkButton(ctx, "Translate", urlTranslate)
      }
      Spacer(modifier = Modifier.height(100.dp))
      val context = LocalContext.current
      ListenButton(context, phrase)
      Spacer(modifier = Modifier.height(100.dp))
      SpeechButton(phrase = phrase)
    }
  }
}

@Composable
fun ListenButton(context: Context, phrase:Phrase, ttsViewModel: TtsViewModel = TtsViewModel()) {

  Button(modifier = Modifier
    .clip(CircleShape)
    .size(90.dp), onClick = {
    ttsViewModel.onListenTrainingPhrase(phrase, context){

    }
  }) {
    Icon(painterResource(id = getTrainingSpeakIcon(isSystemInDarkTheme())),
      modifier = Modifier.scale(2.0f),
      contentDescription = "Start listening")
  }
}
@Composable
fun SpeechButton(phrase: Phrase) {
  val initialText = remember { mutableStateOf(true) }
  val speechDefaultText = remember { mutableStateOf("Your speech will appear here.") }
  val speechText = speechDefaultText
  val defaultTextLabel = stringResource(id = TrainingScreenLabel.TrainingSpeakDefaultText.title)
  val textSize = 20.sp
  val withoutSuffixedOriginText = filterSuffixCharacters(phrase.name.trim().lowercase(), listOf('.', ',', '?', '!'))

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

  Button(modifier = Modifier
    .clip(CircleShape)
    .size(90.dp), onClick = {
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
    Icon(painterResource(id = getTrainingRecordIcon()),
      modifier = Modifier.scale(2.0f),
      contentDescription = "Start speech")
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

@Composable
fun LinkButton(context: Context, buttonLabel: String, link: Uri) {
  Button(onClick = {
    val c = CustomTabsIntent.Builder().build()
    c.intent.setPackage("com.android.chrome")
    try {
      c.launchUrl(context, link)
    }
    catch (e: Exception) {
      // Handle cases where Chrome is not installed or there are other issues
      // Fallback to standard browser intent
      val browserIntent = Intent(Intent.ACTION_VIEW, link)
      if (browserIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(browserIntent)
      } else {
        Toast.makeText(context, "No browser available", Toast.LENGTH_SHORT).show()
      }
    }

  }) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    )
    {
      Icon(
        modifier = Modifier.size(ButtonDefaults.IconSize),
        painter = painterResource(id = R.drawable.icon_baseline_description),
        contentDescription = null)
      Text(text = buttonLabel)
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
  SpeakingPhraseScreen(phrase = Phrase(name = "Sample", pron = "sam-pl"), navController = navController)
}