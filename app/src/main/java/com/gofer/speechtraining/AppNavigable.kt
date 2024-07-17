package com.gofer.speechtraining

import HomeScreen
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class TrainingScreenLabel(@StringRes val title: Int)  {
  TrainingList(R.string.app_name),
  TrainingContents(R.string.training_topic)
}
@Composable
fun AppNavigation() {
  val navController = rememberNavController()
  NavHost(navController = navController, startDestination = TrainingScreenLabel.TrainingList.name) {
    composable(TrainingScreenLabel.TrainingList.name) { HomeScreen(navController) }
//    composable(TrainingScreenLabel.TrainingContents.name) {
//      TrainingContentsScreen()
//    }
  }
}