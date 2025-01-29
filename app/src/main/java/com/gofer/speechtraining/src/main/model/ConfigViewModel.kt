package com.gofer.speechtraining.src.main.model

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.io.IOException
import java.security.GeneralSecurityException
import kotlin.text.isNullOrBlank


class ConfigViewModel(
  application: Application,
  orgNameValue: String,
  projNameValue: String,
  apiKeyValue: String) : AndroidViewModel(application){

    // Constants for preference keys - More readable and maintainable
  companion object {
    private const val PREF_KEY_API_KEY = "api-key"
    private const val PREF_KEY_PROJECT_NAME = "project-name"
    private const val PREF_KEY_ORG_NAME = "org-name"
    private const val PREFS_FILENAME = "speech-training-api.txt"
  }

  // Using a single SharedPreferences instance for better management
  private val _sharedPreferences: SharedPreferences by lazy {
    try {
      val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
      EncryptedSharedPreferences.create(
        PREFS_FILENAME,
        masterKey,
        getApplication(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
      )
    } catch (e: GeneralSecurityException) {
      // Handle security exceptions
      e.printStackTrace()
      // Consider throwing a RuntimeException or returning a default SharedPreferences
      throw RuntimeException("Failed to initialize encrypted shared preferences", e)
    } catch (e: IOException) {
      // Handle IO exceptions
      e.printStackTrace()
      // Consider throwing a RuntimeException or returning a default SharedPreferences
      throw RuntimeException("Failed to initialize encrypted shared preferences", e)
    }
  }

  init{
    writeToSharedPrefs(PREF_KEY_ORG_NAME, orgNameValue)
    writeToSharedPrefs(PREF_KEY_PROJECT_NAME, projNameValue)
    writeToSharedPrefs(PREF_KEY_API_KEY, apiKeyValue)
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
    val apiKey = readFromSharedPrefs(PREF_KEY_API_KEY)
    if (apiKey.isNullOrBlank()) {
      // Handle the case where the API key is not available
      _myLiveData.value = "API key is missing or invalid"
      return false
    }
    val client = OpenAIClient(readFromSharedPrefs(PREF_KEY_API_KEY))
    val listener = OnMessageReceivedListener()

    client.sendMessage(
      input = prompt,
      listener = listener)
    return true
  }

  fun validateApi() : Boolean {
    val apiKey = readFromSharedPrefs(PREF_KEY_API_KEY)

    return apiKey?.isNotBlank() == true
  }
}