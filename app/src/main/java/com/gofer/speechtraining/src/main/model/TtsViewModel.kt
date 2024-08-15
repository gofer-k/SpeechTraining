package com.gofer.speechtraining.src.main.model

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.Locale

class TtsViewModel: ViewModel() {
  private var tts: TextToSpeech? = null

  private val _state = mutableStateOf(String())
//  val state: State<String> = _state

  fun onSpeakTrainingPhrase(phrase: String, context: Context) {
    _state.value = phrase

    textToSpeak(context)
  }
  fun textToSpeak(context: Context) {
    tts = TextToSpeech(context) { status ->
      if (status == TextToSpeech.SUCCESS) {
        Log.d("TextToSpeech", "Initialization Success")
        Log.d("TextToSpeech", "Phrase to speak: ${_state.value}")
        tts?.let {
          val result = it.setLanguage(Locale.US)
          if (result == TextToSpeech.LANG_MISSING_DATA ||
            result == TextToSpeech.LANG_NOT_SUPPORTED
          ) {
            Log.d("SpeechTraining", "Initialize TTS language")
          }
          it.setSpeechRate(1.0f)
          it.speak(_state.value, TextToSpeech.QUEUE_ADD, null, null)
        }
      } else {
        Log.d("TextToSpeech", "Initialization Failed")
      }
    }
  }
}