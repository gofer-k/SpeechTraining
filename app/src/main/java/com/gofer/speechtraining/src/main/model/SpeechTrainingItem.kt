package com.gofer.speechtraining.src.main.model

import com.google.gson.annotations.SerializedName

data class SpeechTrainingItem(
  val lang: String,
  val topic: Topic,
  val phrases: List<Phrase>) {
}
