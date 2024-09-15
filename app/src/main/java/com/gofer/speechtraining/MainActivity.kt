package com.gofer.speechtraining

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.text.intl.LocaleList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gofer.speechtraining.navigation.AppNavigation
import com.gofer.speechtraining.src.main.model.SpeechTrainingData
import com.gofer.speechtraining.src.main.model.SpeechTrainingDataViewModel
import com.gofer.speechtraining.src.main.model.readDataFromSource
import com.gofer.speechtraining.src.main.model.saveSpeakingTrainingDataToFile
import com.gofer.speechtraining.ui.theme.ConfiguredLanguage
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme
import com.google.gson.Gson


//class MainActivity : ComponentActivity() {
class MainActivity : ComponentActivity() {
  var trainingData: SpeechTrainingData? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    val path = baseContext.resources.getString(R.string.app_data_source)
    val trainingAssetDataJson = readDataFromSource(baseContext, path)
    trainingData = Gson().fromJson(trainingAssetDataJson, SpeechTrainingData::class.java)

    setContent {
      SpeechTrainingTheme {
        val viewModel = viewModel<SpeechTrainingDataViewModel>(
        factory = object : ViewModelProvider.Factory {
          override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SpeechTrainingDataViewModel(data = trainingData!!) as T
          }
        })

        val availableLangs = ConfiguredLanguage.entries.filter { lang ->
          LocaleList.current.localeList.any { it.language == lang.lang.locale.language }
        }.map { it.lang }

        viewModel.setAvailableLanguages(availableLangs)

//        PermissionsDialog {
          viewModel.setOrChangePermissionState(NeededPermission.RECORD_AUDIO, true)
          AppNavigation(viewModel)
//        }
      }
    }
  }

  override fun onStop() {
    super.onStop()
    trainingData?.let {
      val path = baseContext.resources.getString(R.string.app_data_source)
      saveSpeakingTrainingDataToFile(baseContext, path, it)
    }
  }
}