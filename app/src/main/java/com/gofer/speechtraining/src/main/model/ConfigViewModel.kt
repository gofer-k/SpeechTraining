package com.gofer.speechtraining.src.main.model

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class ConfigViewModel(
  application: Application,
  orgNameValue: String,
  projNameValue: String,
  apiKeyValue: String) : AndroidViewModel(application){
  private val _apiName = "api-name"
  private val _projName = "project-name"
  private val _orgName = "org-name"

  private var _sharedPreferences: SharedPreferences

  private val _keyAlias by lazy {
    // Although you can define your own key generation parameter specification, it's
    // recommended that you use the value specified here.
    val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    MasterKeys.getOrCreate(keyGenParameterSpec)
  }

//  private val _vaultName = "speech-training-api.txt"

  init{
    _sharedPreferences = lazy {
      //The name of the file on disk will be this, plus the ".xml" extension.
      val sharedPrefsFile = "sharedPrefs"

      //Create the EncryptedSharedPremises using the key above
      EncryptedSharedPreferences.create(
        sharedPrefsFile,
        _keyAlias,
        getApplication(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
      )
    }.value

    writeToSharedPrefs(_orgName, orgNameValue)
    writeToSharedPrefs(_projName, projNameValue)
    writeToSharedPrefs(_apiName, apiKeyValue)
  }

  private fun writeToSharedPrefs(key: String, value: String) {
    with (_sharedPreferences.edit()) {
      putString(key, value)
      apply()
    }
  }

  private fun readFromSharedPrefs(key: String): String? {
    return _sharedPreferences.getString(key, "")
  }

  private var _myLiveData = MutableLiveData<String>()
  val myLiveData: LiveData<String> = _myLiveData

  private fun sentRequest(url: String, headers: Map<String, String>): Boolean {

    viewModelScope.launch(Dispatchers.IO) {
      var connection: HttpURLConnection? = null
      try {
        connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "POST" //"GET" // Or "POST", "PUT", etc.
        connection.doOutput = true // Enable output for POST requests

        for ((key, value) in headers) {
          connection.setRequestProperty(key, value)
        }

        val bodyRequest = """
          {
            "model": "gpt-4o-mini","
            "messages": [
              {
                "role": "user","
                "content": "put pronunciation the word for Polish users: 'Indentifier'"
              }
            ],
            "temperature": 0.7"+
          }""".trimIndent()

        val outputStream = connection.outputStream
        outputStream.write(bodyRequest.toByteArray())
        outputStream.flush()
        outputStream.close()


        val inputStream: InputStream = connection.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
          stringBuilder.append(line)
        }
        reader.close()
        inputStream.close()

        withContext(Dispatchers.Main) {
          _myLiveData.value = stringBuilder.toString()
        }

        Log.d("SpeechTraining", "Response: ${_myLiveData.toString()}")
      }
      catch (e: Throwable) {
        Log.d("SpeechTraining", "Error: ${e.toString()}")
        connection?.run { disconnect() }
      }
      finally {
        connection?.disconnect()
      }
    }
    return true
  }

  fun validateApi() : Boolean {
    val api = readFromSharedPrefs(_apiName)
    val org = readFromSharedPrefs(_orgName)
    val proj = readFromSharedPrefs(_projName)
    var res = false
    api?.run {
      org?.run {
        proj?.run {
          val url = "https://api.openai.com/v1/chat/completions" //"https://api.openai.com/v1/models"
          val headers = mapOf(
            Pair("Content-Type:", "application/json"),
            Pair("Authorization: Bearer", api))
          // ... (Read response from connection) ...
          res= sentRequest(url, headers)
        }
      }
    }
    return res
  }
}