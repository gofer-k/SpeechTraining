package com.gofer.speechtraining.src.main.model

import androidx.lifecycle.ViewModel
import com.gofer.speechtraining.Language
import com.gofer.speechtraining.NeededPermission

class SpeechTrainingDataViewModel (var data: SpeechTrainingData): ViewModel() {
  private var _availableLangs = mutableListOf<Language>()
  val availableLanguages: List<Language> = _availableLangs

  private val _permissions = mutableListOf<NeededPermission>()
  val permissions: List<NeededPermission> = _permissions

  fun getTrainingTopics() = data.getTrainingTopics()

  fun getTrainingTopic(topicId: Long) = data.getTrainingTopicById(topicId)
  fun getTrainingPhrases(trainingId: Long) = data.getTrainingPhrases(trainingId)
  fun setAvailableLanguages(availableLangs: List<Language>) {
    _availableLangs.clear()
    _availableLangs.addAll(availableLangs)
  }

  fun changePermissionState(neededPermission: NeededPermission, state: Boolean) {
    val index = _permissions.indexOfFirst { it.ordinal == neededPermission.ordinal }
    if (index != -1) {
      _permissions[index].isGranted = state
    }
  }
  fun isPermissionGranted(neededPermission: NeededPermission): Boolean {
    return _permissions.firstOrNull { neededPermission.ordinal == it.ordinal }?.isGranted == true
  }

  fun addTrainingPhrase(topicId: Long, phrase: Phrase) {
    data.addPhraseTmTopic(topicId, phrase)
  }
}
