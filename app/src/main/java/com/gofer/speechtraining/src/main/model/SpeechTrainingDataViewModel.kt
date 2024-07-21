package com.gofer.speechtraining.src.main.model

import androidx.lifecycle.ViewModel

class SpeechTrainingDataViewModel (var data: SpeechTrainingData): ViewModel() {
  fun addTrainingTopic(trainingItem: SpeechTrainingItem) {
    data.addTrainingItem(trainingItem);
  }

  fun getTrainingTopicNames() = data.addTrainingTopicNames()

  fun getTrainingTopics() = data.addTopic()
}

class MyApplication {

}
