package com.gofer.speechtraining.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gofer.speechtraining.ImagePicker
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.getDefaultTopicIcon
import com.gofer.speechtraining.ui.theme.Purple40
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTopicScreen(navController: NavController) {
  var topicName by remember { mutableStateOf("") }
  val topicImageUrl by remember { mutableStateOf(URL("file://")) }
  val defaultTopicIcon by remember { mutableIntStateOf(getDefaultTopicIcon()) }

  Scaffold(
   topBar = {
     TopAppBar(
       title = {
         Box(
           modifier = Modifier.fillMaxWidth(),
           contentAlignment = Alignment.Center) {
           Text(text = stringResource(TrainingScreenLabel.TrainingAddTopic.title))
         }
       },
       colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple40)
     )
   },
    bottomBar = {
      BottomAppBar {
        NavigationBarItem(
          selected = false,
          onClick = { navController.popBackStack()},
          icon = { Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null) })
        NavigationBarItem(
          selected = false,
          onClick = {
            if (topicName.isNotBlank()) {
              navController.previousBackStackEntry?.savedStateHandle?.set("addTopic", topicName)
              navController.previousBackStackEntry?.savedStateHandle?.set("addTopicImageUrl", topicImageUrl.toString())
              navController.popBackStack()
            }
          },
          icon = { Icon(imageVector = Icons.Rounded.Done, contentDescription = null) })
      }
    }) { contentPadding ->
   Column(modifier = Modifier
     .fillMaxWidth()
     .padding(contentPadding),
     horizontalAlignment = Alignment.CenterHorizontally,
     verticalArrangement = Arrangement.Center) {
     val keyBoardController = LocalSoftwareKeyboardController.current
     Spacer(
       modifier = Modifier
         .fillMaxWidth()
         .padding(vertical = 24.dp))
     Icon(
       imageVector = Icons.Filled.Build,
       contentDescription = null,
       modifier = Modifier
         .fillMaxWidth()
         .scale(2.0f))
     Spacer(
       modifier = Modifier
         .fillMaxWidth()
         .padding(vertical = 32.dp))
     TextField(
       modifier = Modifier
         .fillMaxWidth()
         .padding(horizontal = 4.dp)
         .width(100.dp), // max text width
       value = topicName,
       label = { Text(text = stringResource(TrainingScreenLabel.TrainingEditPhraseLabel.title))},
       placeholder = { Text(text = stringResource(TrainingScreenLabel.TrainingInputText.title))},
       maxLines = 2, // max visible text lines
       shape = RoundedCornerShape(24.dp),
       keyboardOptions = KeyboardOptions(
         capitalization = KeyboardCapitalization.Sentences,
         keyboardType = KeyboardType.Text,
         // The action property type have to consistent with keyboardActions type
         imeAction = ImeAction.Done),
       keyboardActions = KeyboardActions(
         onDone = {
           keyBoardController?.hide()
         },
       ),
       onValueChange = {topicName = it })
     Spacer(
       modifier = Modifier
         .fillMaxWidth()
         .padding(vertical = 32.dp))
     Text(text = stringResource(id = TrainingScreenLabel.TrainingTopicImage.title), fontSize = 20.sp)
     Image(
       modifier = Modifier.padding(vertical = 4.dp),
       painter = painterResource(id = defaultTopicIcon),
       contentDescription = null)
     Button(
       modifier = Modifier.padding(vertical = 4.dp),
       onClick = {
       }) {
       Text(text = stringResource(id = TrainingScreenLabel.TrainingTopicImageCustomize.title))
       ImagePicker()
     }
   }
  }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun AddTopicScreenPreview() {
  SpeechTrainingTheme {
    val navController = rememberNavController()
    AddTopicScreen(navController)
  }
}