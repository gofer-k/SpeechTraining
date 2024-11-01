package com.gofer.speechtraining.navigation

import TrainingListsScreen
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.TrainingScreenLabel.TrainingAddPhrase
import com.gofer.speechtraining.TrainingScreenLabel.TrainingList
import com.gofer.speechtraining.screens.AddTopicScreen
import com.gofer.speechtraining.screens.AddTrainingDataScreen
import com.gofer.speechtraining.screens.ConfigScreen
import com.gofer.speechtraining.screens.SpeakingPhraseScreen
import com.gofer.speechtraining.screens.TrainingContentScreen
import com.gofer.speechtraining.src.main.model.Phrase
import com.gofer.speechtraining.src.main.model.SpeechTrainingDataViewModel
import com.gofer.speechtraining.toLocale

@Composable
fun AppNavigation(viewModel: SpeechTrainingDataViewModel, onExportAppData: (uri: Uri?) -> Unit) {
  val navHostController = rememberNavController()

  NavHost(navController = navHostController, startDestination = TrainingList.name) {
      composable(TrainingList.name) {backStackEntry ->
        val newTopicName = backStackEntry.savedStateHandle.get<String>("addTopic")
        val newTopicImageUri = backStackEntry.savedStateHandle.get<String>("addTopicImageUrl")
        backStackEntry.savedStateHandle.remove<String>("addTopic")
        backStackEntry.savedStateHandle.remove<String>("addTopic")

        val filterLanguage = viewModel.filterTrainingLanguage.collectAsState()

        newTopicName?.let { name ->
          newTopicImageUri?.let { uri ->
            viewModel.addSpeechTrainingItem(topicName = name, topicImageUri = uri)
          }
        }
        TrainingListsScreen(
          navController = navHostController,
          viewModel.getTrainingTopics(),
          viewModel.availableLanguages,
          selectedLanguage = filterLanguage.value,
          onFilterTrainingLanguage = {
            changedFilterLanguage ->  viewModel.filterTrainingLanguage(changedFilterLanguage)
          },
          onRemoveTopic = { topicId ->
            viewModel.removeTopic(topicId)
          })
      }
      composable("${TrainingScreenLabel.TrainingConfiguration.name}?topicId={topicId}",
        // Navigable view with forward argument
        arguments = listOf(
          navArgument(name = "topicId") {
            type = NavType.LongType
            defaultValue = 0
          }
        )
      ) { backStackEntry ->
        val navArgs = backStackEntry.arguments
        val topicId = navArgs?.getLong("topicId") ?: 0L

        // Navigable previous view (add phrase view)
        val addPhraseText = backStackEntry.savedStateHandle.get<String>("addPhrase")
        val phraseLang = backStackEntry.savedStateHandle.get<String>("phraseLang")
        val phraseCountry = backStackEntry.savedStateHandle.get<String>("phraseCountry")

        addPhraseText?.run {
          phraseLang?.let {language ->
            toLocale(lang = language, country = phraseCountry)
          }?.let {
            viewModel.addTrainingPhrase(topicId, Phrase(name = addPhraseText, language = it))
          }
        }
        backStackEntry.savedStateHandle.remove<String>("addPhrase")
        backStackEntry.savedStateHandle.remove<String>("phraseLang")

        viewModel.getTrainingTopic(topicId = topicId)?.let {
          TrainingContentScreen(navController = navHostController,
            viewModel.getTrainingPhrases(topicId),
            it,
            onPermissionGranted = { neededPermission ->
              viewModel.isPermissionGranted(neededPermission) },
            onDeletePhrase = {topic, phrase ->
              viewModel.removeTrainingPhrase(topic, phrase)
            })
        }
    }
    composable("${TrainingAddPhrase.name}?topicId={topicId}",
      arguments =  listOf(
        navArgument(name = "topicId") {
          type = NavType.LongType
          defaultValue = 0L
        })
    ) { backStackEntry ->
      val topic = backStackEntry.arguments?.getLong("topicId")
        ?.let { viewModel.getTrainingTopic(it) }

      topic?.let {  AddTrainingDataScreen(navController = navHostController, it, viewModel.availableLanguages) }
    }
    composable("SpeakingPhraseScreen?phrase={phrase}&phraseLang={phraseLang}&phraseCountry={phraseCountry",
      arguments = listOf(
        navArgument("phrase"){
          type = NavType.StringType
          defaultValue = ""
        },
        navArgument("phraseLang") {
          type = NavType.StringType
          defaultValue = ""
        },
        navArgument("phraseCountry") {
          type = NavType.StringType
          defaultValue = ""
        }
      )
    ) { backStackEntry ->
      backStackEntry.arguments?.let {
        val name = it.getString("phrase")
        val lang = it.getString("phraseLang")
        val country = it.getString("phraseCountry")

        name?.let { namePhrase ->
          lang?.let {
            toLocale(lang = it, country)
          }?.let { locale ->
            SpeakingPhraseScreen(
              navController = navHostController,
              phrase = Phrase(name = namePhrase, language = locale))
          }
        }
      }
    }
    composable("AddTrainingTopic") {
      AddTopicScreen(navController = navHostController)
    }
    composable("ConfigAppScreen") {
      ConfigScreen(navController = navHostController, omExportAppData = onExportAppData)
    }
  }
}
