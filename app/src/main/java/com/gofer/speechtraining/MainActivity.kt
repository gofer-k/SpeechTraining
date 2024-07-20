package com.gofer.speechtraining

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gofer.speechtraining.src.main.model.ReadJSONFromAssets
import com.gofer.speechtraining.src.main.model.SpeechTrainingData
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val trainingAssetDataJson = ReadJSONFromAssets(baseContext, "speechtrainingdata.json" )
        val data = Gson().fromJson(trainingAssetDataJson, SpeechTrainingData::class.java)
        Log.d("[SpeechTraining model]", "${data.items}")
        setContent {
            SpeechTrainingTheme {
                AppNavigation()
            }
        }
    }
}