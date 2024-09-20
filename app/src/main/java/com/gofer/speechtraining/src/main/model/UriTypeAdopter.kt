package com.gofer.speechtraining.src.main.model

import android.net.Uri
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class UriTypeAdopter: TypeAdapter<Uri>() {
  override fun write(out: JsonWriter?, value: Uri?) {
    out?.value(value?.toString())
  }

  override fun read(`in`: JsonReader?): Uri? {
    return  `in`?.runCatching {
      Uri.parse(nextString())
    }?.getOrNull()
  }
}