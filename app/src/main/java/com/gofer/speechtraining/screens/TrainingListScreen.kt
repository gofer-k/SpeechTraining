
import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
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
import coil.compose.AsyncImage
import com.gofer.speechtraining.TopicDataState
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.getDefaultTopicIcon
import com.gofer.speechtraining.src.main.model.Topic
import com.gofer.speechtraining.ui.theme.PurpleGrey80
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TrainingListsScreen(topics: List<Topic>, navController: NavController) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(id = TrainingScreenLabel.TrainingList.title))
            }
        },
        colors = topAppBarColors(containerColor = PurpleGrey80)
    )},
    bottomBar = {
      BottomAppBar {
        NavigationBarItem(
          selected = false,
          onClick = {
            navController.navigate("AddTrainingTopic")
          },
          icon = { Icon(imageVector = Icons.Rounded.AddCircle, contentDescription = null,  Modifier.scale(2.0f)) })
      }
    }
  ) {paddingValues ->
    Column(modifier = Modifier.padding(paddingValues)) {
      Spacer(modifier = Modifier.height(24.dp))
      ConversationsTopics(navController = navController, topics)
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicItem(navController: NavController,
              topic: Topic,
              onSelectedTopic: (Topic) -> Unit,
              onImageLoading: (Boolean) -> Unit) {
  Card(
        modifier = Modifier
          .size(width = 100.dp, height = 150.dp)
          .padding(8.dp),
        colors = CardDefaults.cardColors(),
        elevation =
        CardDefaults.elevatedCardElevation(
            defaultElevation = 12.dp,
            pressedElevation = 18.dp),
        onClick = {
          onSelectedTopic(topic.copy(isSelected = topic.isSelected.not()))
          navController.navigate(
            "${TrainingScreenLabel.TrainingConfiguration.name}?topicId=${topic.id}") }) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = topic.name,
                style = TextStyle(
                    textIndent = TextIndent(firstLine = 8.sp),
                    color = MaterialTheme.colorScheme.primary),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier
              .fillMaxWidth()
              .height(4.dp))
            AsyncImage(
              model = topic.imageUri,
              contentDescription = stringResource(id = TrainingScreenLabel.TrainingTopicImage.title),
              placeholder = painterResource(id = getDefaultTopicIcon()),
              modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 8.dp, vertical = 4.dp),
              onLoading = {
                onImageLoading(true)
              },
              onSuccess = {
                onImageLoading(false)
              })
            Spacer(modifier = Modifier
              .fillMaxWidth()
              .height(4.dp))
        }
    }
}

@Composable
fun ConversationsTopics(navController: NavController, topics: List<Topic>) {
  val topicListState = remember { TopicDataState() }
  topicListState.setTopicList(topics)

  val loadingCounter = remember { mutableStateOf(topics.size) }

  LaunchedEffect(key1 = topics.size) {}

  Box(contentAlignment = Alignment.Center){
    LazyVerticalGrid(
      modifier = Modifier.alpha(if (loadingCounter.value > 0) 0f else 1f),
      columns = GridCells.Fixed(2),
      contentPadding = PaddingValues(16.dp)
    ) {
      itemsIndexed(topicListState.topicsList) { index, topic ->
        TopicItem(
          navController = navController,
          topic,
          onSelectedTopic = topicListState::onSelectedTopic,
          onImageLoading = { loadingState ->
            if (loadingState == false) {
              --loadingCounter.value
            }
          }
        )
      }
    }
    if (loadingCounter.value > 0) {
      CircularProgressIndicator(modifier = Modifier
        .fillMaxSize()
        .scale(0.5f), strokeWidth = 4.dp)
    }
  }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun TrainingListsScreenPreview() {
    SpeechTrainingTheme {
        val navController = rememberNavController()
        TrainingListsScreen(
            topics = listOf(
                Topic(name = "First"),
                Topic(name = "Second"),
                Topic(name = "Third"),
                Topic(name = "Fourth")), navController = navController)
    }
}

