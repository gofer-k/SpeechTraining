package com.gofer.speechtraining

import androidx.annotation.StringRes
import com.gofer.speechtraining.src.main.model.Topic

enum class TrainingScreenLabel(@StringRes val title: Int)  {
  TrainingApp(R.string.app_name),
  TrainingAppSource(R.string.app_data_source),
  TrainingList(R.string.app_name),
  TrainingConfiguration(R.string.training_configuration),
  TrainingContents(R.string.training_topic),
  TrainingPhraseSpeech(R.string.btn_speak),
  TrainingCancel(R.string.cancel),
  TrainingSave(R.string.save),
  TrainingSpeakingPhrase(R.string.speaking_phrase),
  TrainingAddPhrase(R.string.add_training_phrase),
  TrainingInputText(R.string.edit_phrase_text),
  TrainingEditPhraseLabel(R.string.edit_phrase_label),
  TrainingLanguage(R.string.language),
  TrainingAddTopic(R.string.add_training_topic),
  TrainingEditTopicLabel(R.string.edit_topic_label),
  TrainingTopicImage(R.string.topic_image),
  TrainingTopicImageCustomize(R.string.topic_custom_image)
}

fun getTrainingSpeakIcon(isDarkTheme: Boolean) = if (isDarkTheme) R.drawable.ic_outline_volume_up_24 else R.drawable.ic_outline_volume_up_24_light
fun getTrainingRecordIcon(isDarkTheme: Boolean) = R.drawable.ic_speak_phrase_foreground

fun getTrainingTopicIcon(topic: Topic): Int {
  return when (topic.name) {
    "Prehab" -> return R.drawable.topic_prehab
    "At a restaurant" -> return R.drawable.topic_at_a_restaurant
    "Work" -> return R.drawable.topic_work
    "Hobby" -> return R.drawable.topic_hobby
    else -> getDefaultTopicIcon()
  }
}

fun getDefaultTopicIcon(): Int = R.drawable.topic_default