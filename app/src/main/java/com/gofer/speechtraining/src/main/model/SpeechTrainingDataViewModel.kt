package com.gofer.speechtraining.src.main.model

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.gofer.speechtraining.Language
import com.gofer.speechtraining.NeededPermission

class SpeechTrainingDataViewModel
  (private val savedStateHandle: SavedStateHandle = SavedStateHandle(),
   val packageName: String,
   var data: SpeechTrainingData): ViewModel() {

  private var _availableLanguages = mutableListOf<Language>()
  val availableLanguages: List<Language> = _availableLanguages

  private val _filterKey = "filterLanguage"
  val filterTrainingLanguage = savedStateHandle.getStateFlow(
    _filterKey,
    _availableLanguages.firstOrNull() ?: Language())

  private val _permissions = mutableListOf<NeededPermission>()

  init {
    data.items.asIterable().forEach {
      it.topic.imageUri = resolveImageUriFromResource(it.topic)
    }
    data.sortTrainingPhrases()
  }
  private fun resolveImageUriFromResource(topic: Topic): Uri? {
    val path = "android.resource://$packageName/drawable"
    val fileUrl = when (topic.name) {
      "Prehab" -> "topic_prehab"
      "At a restaurant" -> "topic_at_a_restaurant"
      "Work" -> "topic_work"
      "Hobby" -> "topic_hobby"
      "Greetings" -> "topic_greetings"
      "Hard word" -> "topic_hard_words"
      "Daily routings" -> "topic_daily_routings"
      else -> "topic_default"
    }
    return Uri.parse("$path/$fileUrl")
  }

  fun getTrainingTopics() = data.getTrainingTopics()
  fun getTrainingTopic(topicId: Long) = data.getTrainingTopicById(topicId)
  fun getAvailableTopicId(): Long {
    return data.items.maxByOrNull { it.topic.id }?.let {
      it.topic.id + 1
    } ?: 0
  }
  fun addSpeechTrainingItem(topicName: String, topicImageUri: String) {
    val imageUri = Uri.parse(topicImageUri)
    val newTopic = Topic(id = getAvailableTopicId(), name = topicName, imageUri = imageUri ?: Uri.EMPTY)
  }
  fun getTrainingPhrases(trainingId: Long) = data.getTrainingPhrases(trainingId)
      .filter { it.language.equals(filterTrainingLanguage.value.locale) }

  fun addTrainingPhrase(topicId: Long, phrase: Phrase) {
    data.addPhraseTmTopic(topicId, phrase)
  }
  fun setAvailableLanguages(availableLanguages: List<Language>) {
    _availableLanguages.clear()
    _availableLanguages.addAll(availableLanguages)
    filterTrainingLanguage(_availableLanguages.firstOrNull() ?: Language())
  }
  fun filterTrainingLanguage(language: Language) {
    savedStateHandle[_filterKey] = language
  }

  fun setOrChangePermissionState(neededPermission: NeededPermission, state: Boolean) {
    val index = _permissions.indexOfFirst { it.ordinal == neededPermission.ordinal }
    if (index != -1) {
      _permissions[index].isGranted = state
    } else {
      neededPermission.isGranted = state
      _permissions.add(neededPermission)
    }
  }
  fun isPermissionGranted(neededPermission: NeededPermission): Boolean {
    return _permissions.firstOrNull { neededPermission.ordinal == it.ordinal }?.isGranted == true
  }
}
