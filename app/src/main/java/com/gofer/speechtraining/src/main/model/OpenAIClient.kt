package com.gofer.speechtraining.src.main.model

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

enum class PromptRole(s: String) {
  User("user"),
  System("system")
}
data class OpenAIPrompt(val role: PromptRole = PromptRole.User, val content: String?){
}
data class OpenAIRequest(
  val model: String = "gpt-4o-mini",
  val messages: List<OpenAIPrompt> = listOf(),
  val temperature: Double = 0.7) {
}

data class OpenAiResponseChoice(val text: String = "") {
}
data class OpenAiResponse(val choices: List<OpenAiResponseChoice>? = null) {
}

class AuthInterceptor(private val apiKey: String) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    // Use a constant for the header name
    val requestBuilder = chain.request().newBuilder()
    requestBuilder.header(HEADER_AUTHORIZATION, "$AUTH_SCHEME $apiKey")

    // Consider adding a check for an empty API key
    if (apiKey.isBlank()) {
      // Handle the case where the API key is missing or invalid.
      // You might want to log an error, throw an exception, or return a specific response.
      // For this example, we'll just proceed without the header.
      println("Warning: API key is empty or blank. Request will be made without authorization.")
    } else {
      requestBuilder.header(HEADER_AUTHORIZATION, "$AUTH_SCHEME $apiKey")
    }

    // Build the request only once
    val request = requestBuilder.build()

    // Consider logging the request for debugging
//    println("Request URL: ${request.url}")
//    println("Request Headers: ${request.headers}")
    return chain.proceed(request)
  }

  companion object {
    // 1. Use a constant for the header name
    private const val HEADER_AUTHORIZATION = "Authorization"
    private const val AUTH_SCHEME = "Bearer"
  }
}
interface OpenAPI {
  @Headers("Content-Type: application/json")
  @POST("chat/completions")
  fun createCompletion(
    @Header("Authorization") authorization: String,
    @Body request: OpenAIRequest?
  ): Call<OpenAiResponse?>?
}

class OnMessageReceivedListener {
  fun onMessageReceived(message: String) {
    Log.d("{OpenAIClient]", "Received message: ${message}")
  }
}

class OpenAIClient(val api_key: String?) {
  // Make the Retrofit instance a singleton
  companion object {
    private var retrofit: Retrofit? = null
    fun getRetrofitInstance(apiKey: String): Retrofit {
      if (retrofit == null) {
        val okHttpClient = OkHttpClient.Builder()
          .addInterceptor(AuthInterceptor(apiKey))
          .build()

        retrofit = Retrofit.Builder()
          .client(okHttpClient)
          .baseUrl("https://api.openai.com/v1/")
          .addConverterFactory(GsonConverterFactory.create())
          .build()
      }
      return retrofit!!
    }
  }

  private val openAiApi: OpenAPI by lazy {
    if (api_key == null) {
      throw IllegalStateException("API key is null. Please provide a valid API key.")
    }
    getRetrofitInstance(api_key).create(OpenAPI::class.java)
  }

  fun sendMessage(input: String?, listener: OnMessageReceivedListener) {
    if (api_key == null) {
      listener.onMessageReceived("API key is not set.")
      return
    }
    if (input.isNullOrBlank()) {
      listener.onMessageReceived("Input message is empty.")
      return
    }

    val prompt = OpenAIPrompt(content = input)
    val request = OpenAIRequest(messages = listOf(prompt))

    val call = openAiApi.createCompletion(authorization = "Bearer $api_key", request = request)

    call?.runCatching {
      enqueue(object : Callback<OpenAiResponse?> {
        override fun onResponse(
          call: Call<OpenAiResponse?>,
          response: retrofit2.Response<OpenAiResponse?>
        ) {
          if (response.isSuccessful) {
            val choices = response.body()?.choices
            if (!choices.isNullOrEmpty()) {
              val responseText = choices[0].text
              listener.onMessageReceived(responseText)
            } else {
              listener.onMessageReceived("OpenAIClient: No response received.")
            }
          } else {
            listener.onMessageReceived(
              "OpenAIClient error: ${
                response.errorBody()?.string() ?: response.message()
              }"
            )
          }
        }

        override fun onFailure(call: Call<OpenAiResponse?>, t: Throwable) {
          listener.onMessageReceived("Request failed: ${t.message ?: "Unknown error"}")
        }
      })
    }
  }
}