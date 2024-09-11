package com.gofer.speechtraining.src.main.model

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TtsViewModel: ViewModel() {
  private var tts: TextToSpeech? = null

  private val _state = mutableStateOf(Phrase())

  fun onListenTrainingPhrase(phrase: Phrase, context: Context, onFinishedSpeech: (Boolean) -> Unit) {
    _state.value = phrase

    textToSpeak(context, onFinishedSpeech)
  }
  fun textToSpeak(context: Context, onFinishedSpeech: (Boolean) -> Unit) {
    tts = TextToSpeech(context) { status ->
      if (status == TextToSpeech.SUCCESS) {
        Log.d("TextToSpeech", "Initialization Success")
        tts?.let {
          val result = it.setLanguage(_state.value.language)
          if (result == TextToSpeech.LANG_MISSING_DATA ||
            result == TextToSpeech.LANG_NOT_SUPPORTED
          ) {
            Log.d("SpeechTraining", "Initialize TTS language")
          }
          it.setSpeechRate(1.0f)
          if (it.speak(_state.value.name, TextToSpeech.QUEUE_ADD, null, null) == android.speech.tts.TextToSpeech.SUCCESS) {
            onFinishedSpeech(it.isSpeaking())
          }
        }
      } else {
        Log.d("TextToSpeech", "Initialization Failed")
      }
    }
  }
}