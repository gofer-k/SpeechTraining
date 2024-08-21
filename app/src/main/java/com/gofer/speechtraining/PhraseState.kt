package com.gofer.speechtraining

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import com.gofer.speechtraining.src.main.model.Phrase

class PhraseState {
  var phraseList = mutableStateListOf<Phrase>()

  fun onSelectedPhrase(selectedPhrase: Phrase) {
    val iter = phraseList.listIterator()
    while (iter.hasNext()) {
      val listItem = iter.next()
      iter.set(
        if (listItem.name.equals(selectedPhrase.name)) {
          selectedPhrase
        } else {
          listItem.copy(isSelected = false)
        }
      )
    }
  }

  fun setPhraseList(inputPhraseList: List<Phrase>) {
    phraseList = inputPhraseList.toMutableStateList()
  }
}