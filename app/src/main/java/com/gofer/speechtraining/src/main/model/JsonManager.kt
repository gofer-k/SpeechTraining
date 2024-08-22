package com.gofer.speechtraining.src.main.model

import android.content.Context
import android.util.Log
import com.gofer.speechtraining.TrainingScreenLabel
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

private fun getInputStream(context: Context, path: String): Result<InputStream> {
  return when (context.getFileStreamPath(path).exists()) {
    true -> {
      Result.runCatching { context.openFileInput(path) as InputStream }
    }
    false -> Result.runCatching { context.assets.open(path) }
  }
}

fun readDataFromSource(context: Context, path: String): String {
  var result = String.toString()

  getInputStream(context, path).onSuccess { ios ->
    val bufferedReader = BufferedReader(InputStreamReader(ios))
    val stringBuilder = StringBuilder()
    bufferedReader.useLines { lines ->
      lines.forEach {
        stringBuilder.append(it)
      }
    }
    result = stringBuilder.toString()
    ios.close()
  }.onFailure {
    Log.e(
      "[${TrainingScreenLabel.TrainingApp.name}]",
      "Error reading configuration source: ${it.printStackTrace()}."
    )
  }
  return result
}

fun saveSpeakingTrainingDataToFile(context: Context, path: String, data: SpeechTrainingData) {
  val json = Gson().toJson(data)

  context.runCatching { openFileOutput(path, Context.MODE_APPEND) }
    .onSuccess { ios ->
      ios.runCatching {
        write(json.toByteArray())

        ios.runCatching { flush() }
          .onSuccess {
            ios.runCatching { close() }
          }
      }
      .onSuccess { Log.i(" [${TrainingScreenLabel.TrainingApp.name}]", "Saved configuration data.") }
      .onFailure { Log.e(" [${TrainingScreenLabel.TrainingApp.name}]", "Failed sate configuration data.") }
    }.onFailure {
      Log.e(" [${TrainingScreenLabel.TrainingApp.name}]", "Failed open configuration data.")
    }
}