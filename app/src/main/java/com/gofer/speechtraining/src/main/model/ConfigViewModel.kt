package com.gofer.speechtraining.src.main.model

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys


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

  fun sentRequest(prompt: String): Boolean {
      val client = OpenAIClient(readFromSharedPrefs(_apiName))
      val listener = OnMessageReceivedListener()

       client.sendMessage(
        input = prompt,
        listener = listener)
    return true
  }

  fun validateApi() : Boolean {
    val apiKey = readFromSharedPrefs(_apiName)

    return apiKey?.isNotBlank() == true
  }
}