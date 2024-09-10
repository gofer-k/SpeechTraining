package com.gofer.speechtraining.src.main.model

import androidx.lifecycle.ViewModel
import com.gofer.speechtraining.Language

class SpeechTrainingDataViewModel (var data: SpeechTrainingData): ViewModel() {
  private var _availableLangs = mutableListOf<Language>()
  val availableLanguages: List<Language> = _availableLangs
  fun getTrainingTopics() = data.getTrainingTopics()

  fun getTrainingTopic(topicId: Long) = data.getTrainingTopicById(topicId)
  fun getTrainingPhrases(trainingId: Long) = data.getTrainingPhrases(trainingId)
  fun setAvailableLanguages(availableLangs: List<Language>) {
    _availableLangs.clear()
    _availableLangs.addAll(availableLangs)
  }

  fun addTrainingPhrase(topicId: Long, phrase: Phrase) {
    data.addPhraseTmTopic(topicId, phrase)
  }
}
