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
    val existingItemsMap = this.items.associateBy { it.topic.id }.toMutableMap() // Use ID for lookup

    for (importedItem in importedTrainingData.items) {
      val existingItem = existingItemsMap[importedItem.topic.id]

      if (existingItem != null) {
        // Item exists, recursively merge it
        existingItem.mergeWith(importedItem)
        // No need to re-insert into map if existingItem is modified in-place
      } else {
        // New item, add it to our map (and eventually to the list)
        existingItemsMap[importedItem.topic.id] = importedItem
      }
    }
    // Update the items list from the merged map values
    this.items = existingItemsMap.values.toList()

    // Get items from importedTrainingData that are not already in the current items
//    val newUniqueItems = importedTrainingData.items.filterNot { it in items } // Or use a Set for better performance if items can be large
//    items = items.plus(newUniqueItems)
  }
}
