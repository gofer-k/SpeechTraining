package com.gofer.speechtraining

import androidx.annotation.StringRes

enum class TrainingScreenLabel(@StringRes val title: Int)  {
  TrainingList(R.string.app_name),
  TrainingContents(R.string.training_topic)
}