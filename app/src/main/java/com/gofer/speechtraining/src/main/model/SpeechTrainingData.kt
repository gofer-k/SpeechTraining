package com.gofer.speechtraining.src.main.model

import com.google.gson.annotations.SerializedName

data class SpeechTrainingData(
  @SerializedName("SpeechTrainingItems") var items: List<SpeechTrainingItem>) {

  fun getTrainingPhrases(trainingId: Long) = items.flatMap {
      if (it.topic.id == trainingId) it.phrases else emptyList()
  }

  fun getTrainingTopicById(trainingId: Long) = items.firstOrNull { it.topic.id == trainingId }?.topic

  fun addTrainingItem(trainingItem: SpeechTrainingItem) {
    items = items.plus(trainingItem)
  }

  fun getTrainingTopics() = items.map { it.topic }
  fun addPhraseToTopic(trainingId: Long, phrase: Phrase) {
    items.firstOrNull { it.topic.id == trainingId }?.addPhrase(phrase)
  }
  fun removePhrase(topic: Topic, phrase: Phrase) {
    val item = items.filter { it.topic.id == topic.id }.firstOrNull()
    item?.let { it.phrases = it.phrases.minus(phrase) }
  }
  fun sortTrainingPhrases() {
    items.forEach {
      it.phrases = it.phrases.sortedBy { it.name }
    }
  }
  fun sortTrainingPhrases(topicId: Long) {
    items.filter {it.topic.id == topicId}.forEach {
      it.phrases = it.phrases.sortedBy { it.name }
    }
  }

  fun mergeTrainingData(importedTrainingData: SpeechTrainingData) {
    // implementation merge logic
    items = items.plus(importedTrainingData.items)

  }
}
