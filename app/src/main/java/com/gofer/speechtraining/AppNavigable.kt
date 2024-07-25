package com.gofer.speechtraining

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gofer.speechtraining.src.main.model.SpeechTrainingDataViewModel

@Composable
fun AppNavigation(viewModel: SpeechTrainingDataViewModel) {
  val navHostController = rememberNavController()
  NavHost(navController = navHostController, startDestination = TrainingScreenLabel.TrainingList.name) {
      composable(TrainingScreenLabel.TrainingList.name) { HomeScreen(viewModel.getTrainingTopics(), navController = navHostController) }
      composable("${TrainingScreenLabel.TrainingContents.name}?name={name}&topicId={topicId}",
      arguments = listOf(
        navArgument(name = "name") {
          type =  NavType.StringType
          defaultValue = TrainingScreenLabel.TrainingContents.name
        },
        navArgument(name = "topicId") {
          type = NavType.LongType
          defaultValue = 0
        }
      )
    ) { backStackEntry ->
      TrainingConfigurationScreen(
        viewModel.getTrainingPhrases(backStackEntry.arguments?.getLong("topicId") ?: 0L),
        navController = navHostController,
        backStackEntry.arguments?.getString("name") ?: "default")
    }
  }
}