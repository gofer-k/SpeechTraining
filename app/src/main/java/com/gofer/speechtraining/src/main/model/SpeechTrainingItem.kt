package com.gofer.speechtraining.src.main.model

data class SpeechTrainingItem(
  val lang: String,
  val topic: Topic,
  var phrases: List<Phrase>) {

  fun addPhrase(addPhrase: Phrase) {
    phrases = phrases.plus(addPhrase)
  }
}
