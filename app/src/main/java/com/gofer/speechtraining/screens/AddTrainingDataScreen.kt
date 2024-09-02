package com.gofer.speechtraining.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gofer.speechtraining.Language
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.src.main.model.Phrase
import com.gofer.speechtraining.src.main.model.Topic
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
  val textDefault = stringResource(TrainingScreenLabel.TrainingEditPhraseText.title)

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = stringResource(TrainingScreenLabel.TrainingAddPhrase.title))
          }
        },
        colors = topAppBarColors(containerColor = Purple40))
             },
    bottomBar = {
      BottomAppBar() {
        NavigationBarItem(
          label = { Text(text = stringResource(TrainingScreenLabel.TrainingCancel.title)) },
          selected = false,
          onClick = {
            navController.navigate(
              "${TrainingScreenLabel.TrainingConfiguration.name}?topicId=${trainingTopic.id}")
          },
          icon = { Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null) })
        NavigationBarItem(
          label = { Text(text = stringResource(TrainingScreenLabel.TrainingSave.title)) },
          selected = false,
          onClick = {
            // TODO: Phrase language is not specified
            if (phrase.name.isNotBlank()) {
                phrase.language.runCatching {
                  isO3Language
                }.onSuccess {
                  navController.navigate(
                    "${TrainingScreenLabel.TrainingConfiguration.name}?topicId=${trainingTopic.id}&addPhrase=${phrase.name}&phraseLang=${phrase.language.language}")
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
          .padding(horizontal = 4.dp),
        value = if (phrase.name.isEmpty()) textDefault else phrase.name ,
        shape = RoundedCornerShape(24.dp),
        onValueChange = {
          if(it.isEmpty()) {
            phrase  = phrase.copy(name = "", language = phrase.language)
          }
          else {
            phrase = phrase.copy(name = it, language = phrase.language)
          }
        })
      Spacer(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 48.dp)
      )
      LanguageList(
        languages = availableLanguages,
        onSelectedLanguage = { lang: Locale ->
          phrase = phrase.copy(name = phrase.name, language = lang)
        })
    }
  }
}

@Composable
fun LanguageList(languages: List<Language>, onSelectedLanguage: (Locale) -> Unit) {
  var isExtended by remember { mutableStateOf( false) }
  val label = stringResource(TrainingScreenLabel.TrainingLanguage.title)
  var selectedLanguage by remember { mutableStateOf(Language(label = label)) }

  Column(modifier = Modifier
    .fillMaxWidth()
    .padding(64.dp)) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .background(
          color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray,
          shape = RoundedCornerShape(24.dp)
        )
        .clickable {
          isExtended = !isExtended
          if (!isExtended)
            onSelectedLanguage(selectedLanguage.locale)
        },
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween) {
      Text(modifier = Modifier.padding(horizontal = 12.dp), text = selectedLanguage.label, fontSize = 20.sp)
      Icon(modifier = Modifier.padding(end = 12.dp), imageVector = Icons.Default.List, contentDescription = null)
    }

    if (isExtended) {
      for (lang in languages) {
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier
          .fillMaxWidth()
          .background(
            color = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray,
            shape = RoundedCornerShape(24.dp)
          )
          .height(60.dp)
          .clickable { selectedLanguage = lang }) {
            Text(text = lang.label,
              modifier = Modifier
                .padding(horizontal = 12.dp)
                .align(alignment = Alignment.Center),
              fontSize = 20.sp, color = if(isSystemInDarkTheme()) Color.LightGray else Color.DarkGray
              )
          }
        }
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