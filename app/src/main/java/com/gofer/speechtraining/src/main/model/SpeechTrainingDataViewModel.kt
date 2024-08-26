package com.gofer.speechtraining.src.main.model

import androidx.lifecycle.ViewModel

class SpeechTrainingDataViewModel (var data: SpeechTrainingData): ViewModel() {

  fun getTrainingTopics() = data.getTrainingTopics()

  fun getTrainingTopic(topicId: Long) = data.getTrainingTopicById(topicId)
  fun getTrainingPhrases(trainingId: Long) = data.getTrainingPhrases(trainingId)
}
