package com.gofer.speechtraining.src.main.model

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.lifecycle.ViewModel

class SttViewModel: ViewModel() {
  fun onSpeakTrainingPhrase(phrase: Phrase, context: Context, function: () -> Unit) {
    // TODO: Speech-to-Text (STT)
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

    speechRecognizer.startListening(recognizerIntent)
  }
}