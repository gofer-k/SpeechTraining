package com.gofer.speechtraining.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.gofer.speechtraining.ui.theme.BlueTrainingTopBarr
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TrainingContentScreen(
  navController: NavController,
  phrases: List<Phrase>,
  trainingTopic: Topic,
  onPermissionGranted: (NeededPermission) -> Boolean,
  onDeletePhrase: (Topic, Phrase) -> Unit,
  ttsViewModel: TtsViewModel = TtsViewModel()
) {
  val context = LocalContext.current
  val phraseListState = remember { PhraseState() }
  phraseListState.setPhraseList(phrases)

  // Scaffold floating button height
  var fabHeight by remember { mutableIntStateOf(0) }
  val heightInDp = with(LocalDensity.current) { fabHeight.toDp() }
  val listState = rememberLazyListState( initialFirstVisibleItemIndex = 0 )

  val deletePhraseText = stringResource(id = TrainingScreenLabel.TrainingDeletePhrase.title)

  Scaffold(
    topBar = {
      val title = """${trainingTopic.name}(${phrases.size})"""
      TopAppBar(title = { 
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
          Text(text = title, color = Color.White)
        }
      },
      colors = topAppBarColors(containerColor = BlueTrainingTopBarr)
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
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.Top,
          contentPadding = PaddingValues(bottom = heightInDp + 16.dp),
        ) {
          itemsIndexed(phraseListState.phraseList) { _, phrase ->
            var showMenu by remember { mutableStateOf(false) }
            Box(modifier = Modifier
              .clip(MaterialTheme.shapes.extraSmall)
              .border(BorderStroke(1.dp, MaterialTheme.colorScheme.inverseOnSurface))
              .combinedClickable(
                onClick = {},
                onLongClick = {
                  showMenu = true
                }
              )
              ,contentAlignment = Alignment.CenterStart
            ) {
              Column(modifier = Modifier.fillMaxSize()) {
                Text(
                  modifier = Modifier.fillMaxWidth(),
                  text = phrase.name,
                  style = TextStyle(textIndent = TextIndent(firstLine = 8.sp)),
                  fontSize = 20.sp,
                  fontWeight = if (phrase.isSelected) FontWeight.Bold else FontWeight.Normal)
                Row(modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.Absolute.SpaceBetween) {
                  IconButton(modifier = Modifier.padding(start = 40.dp),
                    onClick = {
                    ttsViewModel.onListenTrainingPhrase(phrase, context
                    ) {
                      phrase.toggle()
                      phraseListState.onSelectedPhrase(phrase)
                    }
                  }) {
                    Icon(
                      painterResource(id = getTrainingSpeakIcon(isSystemInDarkTheme())),
                      contentDescription = TrainingScreenLabel.TrainingPhraseSpeech.name)
                  }
                  IconButton(modifier = Modifier.padding(end = 40.dp),
                    onClick = {
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
                DropdownMenu(
                  modifier = Modifier.wrapContentSize(),
                  expanded = showMenu,
                  onDismissRequest = { showMenu = false }
                ) {
                  DropdownMenuItem(
                    text = {
                      Row {
                        Icon(imageVector = Icons.Default.Delete, deletePhraseText)
                        Text(text = deletePhraseText) }
                    },
                    onClick = {
                      showMenu = false
                      phraseListState.phraseList.remove(phrase)
                      onDeletePhrase(trainingTopic, phrase)
                    }
                  )
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
      list,
      Topic(name = "Topic"),
      onPermissionGranted = { false },
      onDeletePhrase = { _, _ ->})
  }
}