package com.gofer.speechtraining.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gofer.speechtraining.Language
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme
import java.util.Locale

@Composable
fun LanguageList(languages: List<Language>, onSelectedLanguage: (Locale) -> Unit) {
  var isExtended by remember { mutableStateOf( false) }
  val label = stringResource(TrainingScreenLabel.TrainingLanguage.title)
  var selectedLanguage by remember { mutableStateOf(Language(label)) }
  val textSize = 20.sp
  val cornerShape = RoundedCornerShape(24.dp)
  val itemHeight = 40.dp//48.dp
  val textHorizontalPadding = 12.dp
  val backGroundColder = if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray

  Column(modifier = Modifier
    .fillMaxWidth()
    .padding(horizontal = 64.dp))
  {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(itemHeight)
        .background(
          color = backGroundColder,
          shape = cornerShape
        )
        .clickable {
          isExtended = !isExtended
          if (!isExtended)
            onSelectedLanguage(selectedLanguage.locale)
        },
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween) {
      Text(modifier = Modifier.padding(horizontal = textHorizontalPadding),
        text = selectedLanguage.label,
        fontSize = textSize)
      Icon(modifier = Modifier.padding(end = textHorizontalPadding),
        imageVector = Icons.Default.List,
        contentDescription = null)
    }

    if (isExtended) {
      for (lang in languages) {
        Spacer(modifier = Modifier.height(2.dp))
        Box(modifier = Modifier
          .fillMaxWidth()
          .background(
            color = backGroundColder,
            shape = cornerShape
          )
          .height(itemHeight)
          .clickable {
            selectedLanguage = lang
            isExtended = !isExtended
          }) {
          Text(text = lang.label,
            modifier = Modifier
              .padding(horizontal = textHorizontalPadding)
              .align(alignment = Alignment.Center),
            fontSize = textSize,
            color = if(isSystemInDarkTheme()) Color.LightGray else Color.DarkGray)
        }
      }
    }
  }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun LanguageList() {
  SpeechTrainingTheme {
    LanguageList(listOf(), onSelectedLanguage = {})
  }
}