package com.gofer.speechtraining.src.main.model

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.Locale

class SpeechTrainingDataViewModel (var data: SpeechTrainingData): ViewModel() {
  private val _state = mutableStateOf(Phrase())
  val state: State<Phrase> = _state

  private val tts: TextToSpeech? = null

  fun onSpeakTrainingPhrase(phrase: Phrase, context: Context) {
    _state.value = state.value.copy(name = phrase.name, isSelected = phrase.isSelected)
    textToSpeak(context)
  }
  fun textToSpeak(context: Context) {
    tts = TextToSpeech(context) { status ->
      if (status == TextToSpeech.SUCCESS) {
        Log.d("TextToSpeech", "Initialization Success")
        tts?.let {
          val result = it.setLanguage(Locale.US)
          if (result == TextToSpeech.LANG_MISSING_DATA ||
            result == TextToSpeech.LANG_NOT_SUPPORTED
          ) {
            Log.d("SpeechTraining", "Initialize TTS language")
          }
          tts.setSpeechRate(1.0f)
          tts.speak(_state.value.name.trim(), TextToSpeech.QUEUE_ADD, null, null)
        }
      } else {
        Log.d("TextToSpeech", "Initialization Failed")
      }
    }
    tts.stop()
    tts.shutdown()
  }

  fun addTrainingTopic(trainingItem: SpeechTrainingItem) {
    data.addTrainingItem(trainingItem);
  }

  fun addTrainingTopicNames() = data.addTrainingTopicNames()

  fun addTrainingTopics() = data.addTopic()
  fun getTrainingTopics() = data.getTrainingTopics()

  fun getTrainingPhrases(trainingId: Long) = data.getTrainingPhrases(trainingId)
}
