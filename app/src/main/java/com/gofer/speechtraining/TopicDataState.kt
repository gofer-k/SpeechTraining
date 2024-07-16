package com.gofer.speechtraining

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList

class TopicDataState {
    var topicsList = mutableStateListOf<Topic>()

    fun onSelectedTopic(selectedTopic: Topic) {
        val iter = topicsList.listIterator()
        while (iter.hasNext()) {
            val listItem = iter.next()
            iter.set(
                if (listItem.id == selectedTopic.id) {
                    selectedTopic
                } else {
                    listItem.copy(isSelected = false)
                }
            )
        }
    }

    fun setTopicList(inputTopicsList: List<Topic>) {
        topicsList = inputTopicsList.toMutableStateList()
    }
}