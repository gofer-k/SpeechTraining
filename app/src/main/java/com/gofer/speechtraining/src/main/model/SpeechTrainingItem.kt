package com.gofer.speechtraining.src.main.model

data class SpeechTrainingItem(
  val lang: String,
  val topic: Topic,
  val phrases: List<Phrase>) {
}
