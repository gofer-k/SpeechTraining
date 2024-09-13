package com.gofer.speechtraining.src.main.model

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognitionSupport
import android.speech.RecognitionSupportCallback
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.concurrent.Executors

class SttViewModel: ViewModel() {
  private val _state = mutableStateOf(Phrase())

  fun onSpeakTrainingPhrase(phrase: Phrase, context: Context, function: () -> Unit) {
    // TODO: Speech-to-Text (STT)
    _state.value = phrase
    speakToRecord(context)
  }

  fun speakToRecord(context: Context) {
    // Get SpeechRecognizer instance
    if (!SpeechRecognizer.isRecognitionAvailable(context)) {
      // Speech recognition service NOT available
      return
    }
    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

    speechRecognizer.setRecognitionListener(object : RecognitionListener {
      override fun onReadyForSpeech(params: Bundle?) {
        TODO("Not yet implemented")
      }

      override fun onBeginningOfSpeech() {
        TODO("Not yet implemented")
      }

      override fun onRmsChanged(rmsdB: Float) {
        TODO("Not yet implemented")
      }

      override fun onBufferReceived(buffer: ByteArray?) {
        TODO("Not yet implemented")
      }

      override fun onEndOfSpeech() {
        TODO("Not yet implemented")
      }

      override fun onError(error: Int) {
        TODO("Not yet implemented")
        Log.d("SpeechRecognizer", "RecognitionListener.onError: $error")
      }

      override fun onResults(results: Bundle)  {
        val data: ArrayList<String>? = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        Log.d("SpeechRecognizer", "Speech recognition results received: $data")
      }

      override fun onPartialResults(partialResults: Bundle?) {
        TODO("Not yet implemented")
      }

      override fun onEvent(eventType: Int, params: Bundle?) {
        TODO("Not yet implemented")
      }
    })

    val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
      putExtra(
        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
      )
    }
    recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, _state.value.language)

    speechRecognizer.checkRecognitionSupport(recognizerIntent, Executors.newSingleThreadExecutor(),
      object : RecognitionSupportCallback {
        override fun onSupportResult(recognitionSupport: RecognitionSupport) {
          // ...
          Log.d("STT service", "RecognitionSupportCallback.onSupportResult" )
        }

        override fun onError(error: Int) {
          // ...
          Log.d("STT service", "RecognitionSupportCallback.onError" )
        }
      })
    speechRecognizer.startListening(recognizerIntent)
  }

  fun queryReconizetionLangueges(context: Context) {
    val intent = Intent(RecognizerIntent.ACTION_GET_LANGUAGE_DETAILS)
    context.sendOrderedBroadcast(intent, null, object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        if (resultCode == Activity.RESULT_OK) {
          val results = getResultExtras(true)

          // Supported languages
          val prefLang = results.getString(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)
          val allLangs =
            results.getCharSequenceArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)
        }
      }
    }, null, Activity.RESULT_OK, null, null)
  }
}