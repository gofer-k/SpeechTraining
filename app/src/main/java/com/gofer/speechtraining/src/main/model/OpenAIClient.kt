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
    val request = chain.request().newBuilder()
      .header("Authorization", "Bearer $apiKey")
      .build()
    return chain.proceed(request)
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
  fun sendMessage(input: String?, listener: OnMessageReceivedListener) {
    api_key?.let {
      // Create the Retrofit instance with the interceptor
      val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(apiKey = it))
        .build()

      val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api.openai.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

      val openAiApi = retrofit.create(OpenAPI::class.java)
      val request = OpenAIRequest(messages = listOf(OpenAIPrompt(content =  "type\": \"text\", \"text\": ${input}")), temperature = 0.7) // You can adjust the temperature and maxTokens as needed
      val call = openAiApi.createCompletion(authorization = "Bearer ${api_key}", request = request)
      call?.runCatching {
        enqueue(object : Callback<OpenAiResponse?> {
          override fun onResponse(
            call: Call<OpenAiResponse?>,
            response: retrofit2.Response<OpenAiResponse?>
          ) {
            if (response.isSuccessful) {
              val choices: List<OpenAiResponseChoice>? = response.body()!!.choices
              if (choices != null && choices.size > 0) {
                val responseText = choices[0].text
                listener.onMessageReceived(responseText)
              } else {
                listener.onMessageReceived("OpenAIClient: No response received.")
              }
            } else {
              listener.onMessageReceived("OpenAIClient error: ${response.message()}")
            }
          }

          override fun onFailure(call: Call<OpenAiResponse?>, t: Throwable) {
            listener.onMessageReceived("Request failed: ${t.message}")
          }
        })
      }
    }
  }
}