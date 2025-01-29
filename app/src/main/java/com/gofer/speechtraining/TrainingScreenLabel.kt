package com.gofer.speechtraining

import androidx.annotation.StringRes

enum class TrainingScreenLabel(@StringRes val title: Int)  {
  TrainingApp(R.string.app_name),
  TrainingList(R.string.app_name),
  TrainingConfiguration(R.string.training_configuration),
  TrainingSpeakingPhrase(R.string.speaking_phrase),
  TrainingAddPhrase(R.string.add_training_phrase),
  TrainingInputText(R.string.edit_phrase_text),
  TrainingEditPhraseLabel(R.string.edit_phrase_label),
  TrainingAddTopic(R.string.add_training_topic),
  TrainingTopicImage(R.string.topic_image),
  TrainingTopicImageCustomize(R.string.topic_custom_image),
  TrainingLanguageLabel(R.string.trainings_language_label),
  TrainingSpeakDefaultText(R.string.speak_default_text),
  TrainingDeletePhrase(R.string.delete_phrase),
  TrainingDelete(R.string.delete_traiining),
  TrainingAppConfig(R.string.config_app),
  TrainingExportAppData(R.string.upload_data)
}

fun getTrainingSpeakIcon(isDarkTheme: Boolean) = if (isDarkTheme) R.drawable.ic_outline_volume_up_24 else R.drawable.ic_outline_volume_up_24_light
fun getTrainingRecordIcon() = R.drawable.ic_speak_phrase_foreground
fun getDefaultTopicIcon(): Int = R.drawable.topic_default

fun getUploadDataIcon() = R.drawable.ic_upload_data