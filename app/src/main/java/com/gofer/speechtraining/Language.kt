package com.gofer.speechtraining

import java.util.Locale

data class Language(val label: String = "", val locale: Locale = Locale.US) {
}

fun toLocale(lang: String): Locale? {
  return try {
    Locale(lang)
  } catch(e: NullPointerException) {
    null
  }
}