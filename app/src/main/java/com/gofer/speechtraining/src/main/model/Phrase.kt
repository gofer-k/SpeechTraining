package com.gofer.speechtraining.src.main.model

data class Phrase(val name: String = "", var isSelected: Boolean = false) {
  fun toggle() {
    isSelected = isSelected.not()
  }
}
