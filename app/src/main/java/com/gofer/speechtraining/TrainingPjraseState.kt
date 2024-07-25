package com.gofer.speechtraining

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import com.gofer.speechtraining.src.main.model.Phrase

 class TrainingPhrasesState {
  var traininghrases = mutableStateListOf<Phrase>()
    fun setPhrases(phrases: List<Phrase>) {
      traininghrases = phrases.toMutableStateList()
    }
}