package com.gofer.speechtraining.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.TrainingScreenLabel.TrainingAddPhrase
import com.gofer.speechtraining.TrainingScreenLabel.TrainingList
import com.gofer.speechtraining.screens.AddTrainingDataScreen
import com.gofer.speechtraining.screens.TrainingConfigurationScreen
import com.gofer.speechtraining.src.main.model.Phrase
import com.gofer.speechtraining.src.main.model.SpeechTrainingDataViewModel
import com.gofer.speechtraining.toLocale

@Composable
fun AppNavigation(viewModel: SpeechTrainingDataViewModel) {
  val navHostController = rememberNavController()
  NavHost(navController = navHostController, startDestination = TrainingList.name) {
      composable(TrainingList.name) {
        HomeScreen(viewModel.getTrainingTopics(), navController = navHostController) }
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

        addPhraseText?.run {
          phraseLang?.let {
            toLocale(it)
          }?.let {
            viewModel.addTrainingPhrase(topicId, Phrase(name = addPhraseText, language = it))
          }
        }

        backStackEntry.savedStateHandle.remove<String>("addPhrase")
        backStackEntry.savedStateHandle.remove<String>("phraseLang")
        // TODO: Add new phrase into view model as well refresh Topic phrases
        viewModel.getTrainingTopic(topicId = topicId)?.let {
          TrainingConfigurationScreen(navController = navHostController,
            viewModel.getTrainingPhrases(topicId),
            it)
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
  }
}
