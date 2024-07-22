package com.gofer.speechtraining

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import com.gofer.speechtraining.src.main.model.Phrase

class TrainingItemsState {
  var trainingItems = mutableStateListOf<Phrase>()
    private set

  fun setTrainingItems(inputTrainingItems: List<Phrase>) {
    trainingItems = inputTrainingItems.toMutableStateList()
  }
}