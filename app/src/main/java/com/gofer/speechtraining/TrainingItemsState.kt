package com.gofer.speechtraining

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList

class TrainingItemsState {
  var trainingItems = mutableStateListOf<String>()

  fun setTrainingItems(inputTrainingItems: List<String>) {
    trainingItems = inputTrainingItems.toMutableStateList()
  }
}