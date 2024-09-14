package com.gofer.speechtraining.src.main.model

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SttViewModel: ViewModel() {
  private val _state = mutableStateOf(Phrase())
  private var _speechRecognizer: SpeechRecognizer? = null
  private var _recognizerIntent: Intent? = null

  private val _recognitionListener = object : RecognitionListener {
    override fun onReadyForSpeech(params: Bundle?) {
      Log.d("SpeechRecognizer", "onReadyForSpeech")
    }

    override fun onBeginningOfSpeech() {
      Log.d("SpeechRecognizer", "An user begun speaking")
    }

    override fun onRmsChanged(rmsdB: Float) {
      Log.d("SpeechRecognizer", "onRmsChanged")
    }

    override fun onBufferReceived(buffer: ByteArray?) {
      Log.d("SpeechRecognizer", "onBufferReceived")
    }

    override fun onEndOfSpeech() {
      Log.d("SpeechRecognizer", "An user ended speaking")
      _speechRecognizer?.run { stopListening() }
    }

    override fun onError(error: Int) {
      Log.d("SpeechRecognizer", "RecognitionListener.onError: $error")
//      resetSpeechRecognizer()
//      startListening()
    }

    override fun onResults(results: Bundle)  {
      Log.d("SpeechRecognizer", "Speech recognition results received: $results")
      val matches: ArrayList<String>? = results
        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
      var text = ""
      matches?.let {
        for (result in it)
          text += """ $result """.trimIndent()

        startListening()
//        if (IS_CONTINUES_LISTEN) {
//          startListening()
//        } else {
//          binding.progressBar1.visibility = View.GONE
//        }
      }
    }

    override fun onPartialResults(partialResults: Bundle?) {
      TODO("Not yet implemented")
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
      TODO("Not yet implemented")
    }
  }

  fun onSpeakTrainingPhrase(phrase: Phrase, context: Context, function: () -> Unit) {
    _state.value = phrase
    speakToRecord(context)
  }

  fun speakToRecord(context: Context) {
    resetSpeechRecognizer(context)
    // Get SpeechRecognizer instance
   if (!SpeechRecognizer.isRecognitionAvailable(context)) {
     // Speech recognition service NOT available
     return
   }
    setRecogniserIntent()
    startListening()
//    _speechRecognizer?.checkRecognitionSupport(recognizerIntent, Executors.newSingleThreadExecutor(),
//      object : RecognitionSupportCallback {
//        override fun onSupportResult(recognitionSupport: RecognitionSupport) {
//          // ...
//          Log.d("STT service", "RecognitionSupportCallback.onSupportResult" )
//          _speechRecognizer.startListening(recognizerIntent)
//        }
//        override fun onError(error: Int) {
//          // ...
//          Log.d("STT service", "RecognitionSupportCallback.onError" )
//          resetSpeechRecognizer()
//        }
//      })
//    _speechRecognizer.startListening(recognizerIntent)
  }

  private fun startListening() {
    _speechRecognizer?.run {
      _recognizerIntent?.run { startListening(_recognizerIntent) }
    }
  }
  private fun resetSpeechRecognizer(context: Context) {
    _speechRecognizer?.run { destroy() }
    _speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    if (SpeechRecognizer.isRecognitionAvailable(context))
        _speechRecognizer?.run { setRecognitionListener(_recognitionListener) }
  }

  private fun setRecogniserIntent() {
    _recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
      putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, _state.value.language)
      putExtra(RecognizerIntent.EXTRA_LANGUAGE, _state.value.language)
      putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//      putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, RESULTS_LIMIT)
    }
  }
}