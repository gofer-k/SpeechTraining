package com.gofer.speechtraining

import androidx.annotation.StringRes

enum class TrainingScreenLabel(@StringRes val title: Int)  {
  TrainingList(R.string.app_name),
  TrainingConfiguration(R.string.training_configuration),
  TrainingContents(R.string.training_topic),
  TrainingPhraseSpaak(R.string.btn_speak),
  TrainingPhraseSpeakIcon(R.drawable.ic_outline_volume_up_24_light),
  TrainingPhraseSpeakDark(R.drawable.ic_outline_volume_up_24)
}

fun getTrainingSpeakIcon(isDarkTheme: Boolean) = if (isDarkTheme) R.drawable.ic_outline_volume_up_24 else R.drawable.ic_outline_volume_up_24_light