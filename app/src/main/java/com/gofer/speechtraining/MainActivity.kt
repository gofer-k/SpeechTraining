package com.gofer.speechtraining

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.intl.LocaleList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gofer.speechtraining.navigation.AppNavigation
import com.gofer.speechtraining.src.main.model.JsonManager
import com.gofer.speechtraining.src.main.model.SpeechTrainingData
import com.gofer.speechtraining.src.main.model.SpeechTrainingDataViewModel
import com.gofer.speechtraining.ui.theme.ConfiguredLanguage
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme

class MainActivity : AppCompatActivity() {
  private val jsonManager = JsonManager()

  private lateinit var viewModel: SpeechTrainingDataViewModel
//  lateinit var configViewModel: ConfigViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    val path = baseContext.resources.getString(R.string.app_data_source)
    val trainingData: SpeechTrainingData =
      jsonManager.readDataFromSource(baseContext, path, SpeechTrainingData::class.java)

    setContent {
      SpeechTrainingTheme {
        viewModel = viewModel<SpeechTrainingDataViewModel>(
        factory = object : ViewModelProvider.Factory {
          override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SpeechTrainingDataViewModel(
              packageName = baseContext.packageName,
              initialData = trainingData) as T
          }
        })

        val availableLangs = ConfiguredLanguage.entries.filter { lang ->
          LocaleList.current.localeList.any { it.language == lang.lang.locale.language }
        }.map { it.lang }

//        configViewModel = ConfigViewModel(application,
//          orgNameValue = BuildConfig.OPENAI_ORG,
//          projNameValue = BuildConfig.OPENAI_PROJECT_SPEECH_TRAINING,
//          apiKeyValue = BuildConfig.OPENAI_KEY)
//        configViewModel.validateApi()
//        configViewModel.sentRequest("put pronunciation the word for Polish users: 'Identifier'")

        viewModel.setAvailableLanguages(availableLangs)
        viewModel.setOrChangePermissionState(NeededPermission.RECORD_AUDIO, true)
        AppNavigation(viewModel, onExportAppData = {
          exportAppData(it)
        })
      }
    }
  }

  private fun exportAppData(uri: Uri?) {
    uri?.let {
      if (it.isAbsolute && it.scheme != null && it.host != null) {
        jsonManager.saveSpeakingTrainingDataToFile(baseContext, it, viewModel.data.value)
      }
    }
  }
  override fun onStop() {
    super.onStop()
      val path = baseContext.resources.getString(R.string.app_data_source)
      jsonManager.saveSpeakingTrainingDataToFile<SpeechTrainingData>(baseContext, path, viewModel.data.value)
  }
}