package com.gofer.speechtraining.src.main.model

import android.util.Log
import com.gofer.speechtraining.TrainingScreenLabel
import java.util.Locale


data class Phrase(val name: String = "", var isSelected: Boolean = false, val language: Locale = Locale.US) {
  fun toggle() {
    isSelected = isSelected.not()
  }

  companion object {
    fun toLocale(lang: String): Locale {
      runCatching { Locale(lang) }
        .onSuccess { return it }
        .onFailure {
          Log.e("[${TrainingScreenLabel.TrainingApp.name}]", "Failed phrase language conversion")
        }
      return Locale.getDefault()
    }
  }
}

