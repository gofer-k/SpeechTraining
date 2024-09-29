package com.gofer.speechtraining.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gofer.speechtraining.NeededPermission
import com.gofer.speechtraining.PhraseState
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.getTrainingRecordIcon
import com.gofer.speechtraining.getTrainingSpeakIcon
import com.gofer.speechtraining.src.main.model.Phrase
import com.gofer.speechtraining.src.main.model.Topic
import com.gofer.speechtraining.src.main.model.TtsViewModel
import com.gofer.speechtraining.ui.compose.LazyColumnScrollPosition
import com.gofer.speechtraining.ui.theme.Pink80
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingContentScreen(
  navController: NavController,
  phrases: List<Phrase>,
  trainingTopic: Topic,
  onPermissionGranted: (NeededPermission) -> Boolean,
  ttsViewModel: TtsViewModel = TtsViewModel()
) {
  val context = LocalContext.current
  val phraseListState = remember { PhraseState() }
  phraseListState.setPhraseList(phrases)

  // Scaffold floating button height
  var fabHeight by remember { mutableIntStateOf(0) }
  val heightInDp = with(LocalDensity.current) { fabHeight.toDp() }
  val listState = rememberLazyListState( initialFirstVisibleItemIndex = 0 )

  Scaffold(
    topBar = {
      TopAppBar(title = { 
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
          Text(text = trainingTopic.name)
        }
      },
      colors = topAppBarColors(containerColor = Pink80)
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        modifier = Modifier.onGloballyPositioned { fabHeight = it.size.height },
        onClick = {}) {
        IconButton(onClick = {
          navController.navigate(
            "${TrainingScreenLabel.TrainingAddPhrase.name}?topicId=${trainingTopic.id}")
        }) {
          Icon(imageVector = Icons.Filled.Add, contentDescription = null )
        }
      }
    },
    floatingActionButtonPosition = FabPosition.End
  ) { scaffoldContentPadding ->
    Box(modifier = Modifier
      .fillMaxSize()
      .padding(scaffoldContentPadding)) {
        LazyColumn(
          state = listState,
          modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight(),
          verticalArrangement = Arrangement.Top,
          contentPadding = PaddingValues(bottom = heightInDp + 16.dp),
        ) {
          itemsIndexed(phraseListState.phraseList) { _, phrase ->
            Box(modifier = Modifier
              .clip(MaterialTheme.shapes.extraSmall)
              .border(BorderStroke(1.dp, MaterialTheme.colorScheme.inverseOnSurface))
              ,contentAlignment = Alignment.CenterStart
            ) {
              Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                  modifier = Modifier.fillMaxWidth(),
                  text = phrase.name,
                  style = TextStyle(textIndent = TextIndent(firstLine = 8.sp)),
                  fontSize = 20.sp,
                  fontWeight = if (phrase.isSelected) FontWeight.Bold else FontWeight.Normal)
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                  IconButton(onClick = {
                    ttsViewModel.onListenTrainingPhrase(phrase, context
                    ) {
                      phrase.toggle()
                      phraseListState.onSelectedPhrase(phrase)
                    }
                  }) {
                    Icon(
                      painterResource(id = getTrainingSpeakIcon(isSystemInDarkTheme())),
                      contentDescription = TrainingScreenLabel.TrainingPhraseSpeech.name) }
                  IconButton(onClick = {
                    if (onPermissionGranted(NeededPermission.RECORD_AUDIO)) {
                      navController.navigate("SpeakingPhraseScreen?phrase=${phrase.name}&phraseLang=${phrase.language.language}")
                    } else {
                      Toast.makeText(context, "Record audio not availability", Toast.LENGTH_SHORT).show()
                    }
                  }) {
                    Icon(
                      painterResource(id = getTrainingRecordIcon(isSystemInDarkTheme())),
                      contentDescription = null)
                  }
                }
              }
            }
          }
       }
       LazyColumnScrollPosition(listState, phraseListState.phraseList)
    }
  }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TrainingContentScreenPreview() {
  SpeechTrainingTheme {
    val navController = rememberNavController()
    val list = mutableListOf<Phrase>()
    for(i in (0 ..20)) {
      list.add(Phrase("item $i"))
    }
    TrainingContentScreen(navController = navController,
      list, Topic(name = "Topic"), onPermissionGranted = {false})
  }
}