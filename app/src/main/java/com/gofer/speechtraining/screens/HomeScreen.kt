
import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.gofer.speechtraining.TopicDataState
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.getTrainingTopicIcon
import com.gofer.speechtraining.src.main.model.Topic
import com.gofer.speechtraining.ui.theme.PurpleGrey80
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(topics: List<Topic>, navController: NavController) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(id = TrainingScreenLabel.TrainingList.title))
                }
            },
            colors = topAppBarColors(containerColor = PurpleGrey80)
        )
    }) {paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Spacer(modifier = Modifier.height(24.dp))
            ConversationsTopics(topics,navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicItem(topic: Topic, onSelectedTopic: (Topic) -> Unit, navController: NavController) {
    val imageResourceId = getTrainingTopicIcon(topic)
    val imageId by remember {  mutableIntStateOf(imageResourceId) }

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
            Image(
               modifier = Modifier
                   .size(width = 80.dp, height = 80.dp)
                   .align(Alignment.CenterHorizontally),
               painter = painterResource(id = imageId), contentDescription = null)
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(4.dp))
        }
    }
}

@Composable
fun ConversationsTopics(topics: List<Topic>, navController: NavController
) {
    val topicListState = remember { TopicDataState() }
    topicListState.setTopicList(topics)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp)
    ) {
        itemsIndexed(topicListState.topicsList) { index, topic ->
            TopicItem(
                topic, onSelectedTopic = topicListState::onSelectedTopic,
                navController = navController
            )
        }
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun HomeScreenPreview() {
    SpeechTrainingTheme {
        val navController = rememberNavController()
        HomeScreen(
            topics = listOf(
                Topic(name = "First"),
                Topic(name = "Second"),
                Topic(name = "Third"),
                Topic(name = "Fourth")), navController = navController)
    }
}

