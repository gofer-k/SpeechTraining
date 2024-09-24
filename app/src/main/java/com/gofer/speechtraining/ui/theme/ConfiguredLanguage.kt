package com.gofer.speechtraining.ui.theme

import com.gofer.speechtraining.Language
import java.util.Locale
enum class ConfiguredLanguage(val lang: Language) {
  English(Language(label = "English", locale = Locale("en", "US"))),
  Polish(Language(label = "Polish", locale = Locale("pl", "PL")))
}