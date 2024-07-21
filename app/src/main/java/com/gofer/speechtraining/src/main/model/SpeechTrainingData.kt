package com.gofer.speechtraining.src.main.model

import com.google.gson.annotations.SerializedName

data class SpeechTrainingData(
  @SerializedName("SpeechTrainingItems") var items: List<SpeechTrainingItem>) {
  fun addTrainingItem(trainingItem: SpeechTrainingItem) {
    items.plus(trainingItem)
  }

  fun addTrainingTopicNames() = items.map { it.topic.name }.toList()

  fun addTopic() = items.map { it.topic }.toList()

}
