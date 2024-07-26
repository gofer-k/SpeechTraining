package com.gofer.speechtraining

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gofer.speechtraining.src.main.model.ReadJSONFromAssets
import com.gofer.speechtraining.src.main.model.SpeechTrainingData
import com.gofer.speechtraining.src.main.model.SpeechTrainingDataViewModel
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme
import com.google.gson.Gson
import java.util.Locale


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val trainingAssetDataJson = ReadJSONFromAssets(baseContext, "speechtrainingdata.json" )
        val trainingData = Gson().fromJson(trainingAssetDataJson, SpeechTrainingData::class.java)


      setContent {
            SpeechTrainingTheme {
                val viewModel = viewModel<SpeechTrainingDataViewModel>(
                  factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                      return SpeechTrainingDataViewModel(data = trainingData) as T
                    }
                  }
                )
                AppNavigation(viewModel)
            }
        }
    }

  override fun onDestroy() {
    super.onDestroy()
    tts.stop()
    tts.shutdown()
  }
}