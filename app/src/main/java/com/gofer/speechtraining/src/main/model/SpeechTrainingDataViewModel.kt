package com.gofer.speechtraining.src.main.model

import androidx.lifecycle.ViewModel

class SpeechTrainingDataViewModel (var data: SpeechTrainingData): ViewModel() {

  fun addTrainingTopic(trainingItem: SpeechTrainingItem) {
    data.addTrainingItem(trainingItem);
  }

  fun addTrainingTopicNames() = data.addTrainingTopicNames()

  fun addTrainingTopics() = data.addTopic()
  fun getTrainingTopics() = data.getTrainingTopics()

}

class MyApplication {

}
