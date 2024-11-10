package com.gofer.speechtraining.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import com.gofer.speechtraining.src.main.model.Phrase

class PhraseState {
  var phraseList = mutableStateListOf<Phrase>()

  fun setPhraseList(inputPhraseList: List<Phrase>) {
    phraseList = inputPhraseList.toMutableStateList()
  }
}