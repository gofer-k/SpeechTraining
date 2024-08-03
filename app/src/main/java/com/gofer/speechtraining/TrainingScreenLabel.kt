package com.gofer.speechtraining

import androidx.annotation.StringRes

enum class TrainingScreenLabel(@StringRes val title: Int)  {
  TrainingList(R.string.app_name),
  TrainingConfiguration(R.string.training_configuration),
  TrainingContents(R.string.training_topic)
}