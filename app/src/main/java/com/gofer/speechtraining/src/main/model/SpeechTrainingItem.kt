package com.gofer.speechtraining.src.main.model

data class SpeechTrainingItem(
  var topic: Topic,
  var phrases: List<Phrase>) {

  fun mergeWith(other: SpeechTrainingItem) {
    // Merge properties. Example:
    this.topic = other.topic // Simple overwrite for name

    // For lists like 'phrases', you might want to add unique new phrases
    val newUniquePhrases = other.phrases.filterNot { it in this.phrases }
    this.phrases = this.phrases.plus(newUniquePhrases)
  }

  fun addPhrase(addPhrase: Phrase) {
    phrases = phrases.plus(addPhrase)
  }
}
