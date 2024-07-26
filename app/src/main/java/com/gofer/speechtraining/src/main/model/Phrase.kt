package com.gofer.speechtraining.src.main.model

data class Phrase(val name: String = "", var isSelected: Boolean = false) {
  fun toggle() {
    isSelected = isSelected.not()
  }

  fun selected(): Boolean {
    return isSelected == true
  }
}
