package com.gofer.speechtraining.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.screens.AddTrainingDataScreen
import com.gofer.speechtraining.screens.TrainingConfigurationScreen
import com.gofer.speechtraining.src.main.model.SpeechTrainingDataViewModel

@Composable
fun AppNavigation(viewModel: SpeechTrainingDataViewModel) {
  val navHostController = rememberNavController()
  NavHost(navController = navHostController, startDestination = TrainingScreenLabel.TrainingList.name) {
      composable(TrainingScreenLabel.TrainingList.name) {
        HomeScreen(viewModel.getTrainingTopics(), navController = navHostController) }
      composable(
        "${TrainingScreenLabel.TrainingConfiguration.name}?topicId={topicId}&addPhrase={addPhrase}&phraseLang={phraseLang}",
      arguments = listOf(
        navArgument(name = "topicId") {
          type = NavType.LongType
          defaultValue = 0
        },
        navArgument(name = "addPhrase") {
          type = NavType.StringType
          defaultValue = ""
        },
        navArgument(name = "phraseLang") {
          type = NavType.StringType
          defaultValue = ""
        }
      )
    )
      { backStackEntry ->
        // TODO: Add new phrase into view model as well refresh Topic phrases
        val navArgs = backStackEntry.arguments
        val topicId = navArgs?.getLong("topicId") ?: 0L
        val addPhraseText = navArgs?.getString("addPhrase") ?: ""
        val phraseLang = navArgs?.getString("phraseLang") ?: ""

        viewModel.getTrainingTopic(topicId = topicId)?.let {
          TrainingConfigurationScreen(navController = navHostController,
            viewModel.getTrainingPhrases(topicId),
            it)
        }
    }
    composable("${TrainingScreenLabel.TrainingAddPhrase.name}?topicId={topicId}",
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