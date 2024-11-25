package com.gofer.speechtraining.src.main.model

import java.util.Locale


data class Phrase(val name: String = "", val pron: String = "", var isSelected: Boolean = false, val language: Locale = Locale.US) {
  fun toggle() {
    isSelected = isSelected.not()
  }
}

