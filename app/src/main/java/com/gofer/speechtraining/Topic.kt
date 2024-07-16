package com.gofer.speechtraining

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource

data class Topic(
    val id: Long = 0L,
    val name: String = "Add training topic",
    val isSelected: Boolean = false
)

@Composable
fun topicsList(): List<Topic> = stringArrayResource(id = R.array.topics).mapIndexed { index, s ->
        Topic(id = index.toLong(), name = s) }