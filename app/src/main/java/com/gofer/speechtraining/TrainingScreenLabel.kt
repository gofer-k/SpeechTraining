package com.gofer.speechtraining

import androidx.annotation.StringRes

enum class TrainingScreenLabel(@StringRes val title: Int)  {
  TrainingApp(R.string.app_name),
  TrainingAppSource(R.string.app_data_source),
  TrainingList(R.string.app_name),
  TrainingConfiguration(R.string.training_configuration),
  TrainingContents(R.string.training_topic),
  TrainingPhraseSpeech(R.string.btn_speak),
  TrainingCancel(R.string.cancel),
  TrainingSave(R.string.save)
}

fun getTrainingSpeakIcon(isDarkTheme: Boolean) = if (isDarkTheme) R.drawable.ic_outline_volume_up_24 else R.drawable.ic_outline_volume_up_24_light