package com.gofer.speechtraining.src.main.model

import com.google.gson.annotations.SerializedName

data class SpeechTrainingData(
  @SerializedName("SpeechTrainingItems") var items: List<SpeechTrainingItem>) {

  fun getTrainingPhrases(trainingId: Long) = items.flatMap {
      if (it.topic.id == trainingId) it.phrases else emptyList()
  }

  fun getTrainingTopicById(trainingId: Long) = items.firstOrNull { it.topic.id == trainingId }?.topic

  fun addTrainingItem(trainingItem: SpeechTrainingItem) {
    items.plus(trainingItem)
  }

  fun addTrainingTopicNames() = items.map { it.topic.name }.toList()

  fun addTopic() = items.map { it.topic }.toList()
  fun getTrainingTopics() = items.map { it.topic }
}
