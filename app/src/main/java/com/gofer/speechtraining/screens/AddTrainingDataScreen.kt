package com.gofer.speechtraining.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.src.main.model.Phrase
import com.gofer.speechtraining.src.main.model.Topic
import com.gofer.speechtraining.ui.compose.Language
import com.gofer.speechtraining.ui.compose.LanguageList
import com.gofer.speechtraining.ui.theme.Purple40
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddTrainingDataScreen(
  navController: NavController,
  trainingTopic: Topic,
  availableLanguages: List<Language>
) {
  var phrase by remember { mutableStateOf(Phrase()) }
  val topicId by remember { mutableLongStateOf(trainingTopic.id) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
              text = stringResource(TrainingScreenLabel.TrainingAddPhrase.title),
              color = Color.White)
          }
        },
        colors = topAppBarColors(containerColor = Purple40))
             },
    bottomBar = {
      BottomAppBar {
        NavigationBarItem(
          selected = false,
          onClick = {
            // To prevent cyclist between previous and the current view
            navController.popBackStack()
          },
          icon = { Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null) })
        NavigationBarItem(
          selected = false,
          onClick = {
            if (phrase.name.isNotBlank()) {
                phrase.language.runCatching {
                  isO3Language
                }.onSuccess {
                  // Pass the result arguments to previous view (TrainingConfiguration view)
                  navController.previousBackStackEntry?.savedStateHandle?.set("topicId", topicId)
                  navController.previousBackStackEntry?.savedStateHandle?.set("addPhrase", phrase.name)
                  navController.previousBackStackEntry?.savedStateHandle?.set("phraseLang", phrase.language.language)
                  navController.previousBackStackEntry?.savedStateHandle?.set("phraseCountry", phrase.language.country)
                  // To prevent cyclist between previous and the current view
                  navController.popBackStack()
                }.onFailure {
                  Log.e(
                    "[${TrainingScreenLabel.TrainingApp.name}]",
                    "Not expected specified phrase language")
                }
            }

          },
          icon = { Icon(imageVector = Icons.Rounded.Done, contentDescription = null) })
      }
    }
  ) { contentPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues = contentPadding)) {
      val keyBoardController = LocalSoftwareKeyboardController.current
      Spacer(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 24.dp)
      )
      Icon(
        imageVector = Icons.Filled.Build,
        contentDescription = null,
        modifier = Modifier
          .fillMaxWidth()
          .scale(2.0f)
      )
      Spacer(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 48.dp)
      )
      TextField(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 4.dp)
          .width(100.dp), // max text width
        value = phrase.name,
        label = { Text(text = stringResource(TrainingScreenLabel.TrainingEditPhraseLabel.title))},
        placeholder = { Text(text = stringResource(TrainingScreenLabel.TrainingInputText.title))},
        maxLines = 2, // max visible text lines
        shape = RoundedCornerShape(24.dp),
        keyboardOptions = KeyboardOptions(
          capitalization = KeyboardCapitalization.Sentences,
          keyboardType = KeyboardType.Text,
          // The action property type have to consistent with keyboardActions type
          imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
          onDone = {
            keyBoardController?.hide()
          },
        ),
        onValueChange = {
          phrase = phrase.copy(name = it, language = phrase.language)
         })
      Spacer(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 48.dp)
      )
      LanguageList(languages = availableLanguages,
        viewModelLanguage = Language(),
        onSelectedLanguage = { lang: Language ->
          phrase = phrase.copy(name = phrase.name, language = lang.locale)
        })
    }
  }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun AddTrainingDataScreenPreview() {
  SpeechTrainingTheme {
    val navController = rememberNavController()
    AddTrainingDataScreen(
      navController = navController,
      trainingTopic = Topic(),
      availableLanguages = listOf(Language("English", Locale("en")), Language("polish", Locale("pl")))
    )
  }
}