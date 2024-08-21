
import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gofer.speechtraining.TopicDataState
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.src.main.model.Topic
import com.gofer.speechtraining.ui.theme.PurpleGrey80
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(topics: List<Topic>, navController: NavController) {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = TrainingScreenLabel.TrainingList.name)
                }
            },
            colors = topAppBarColors(containerColor = PurpleGrey80)
        )
    }) {
        Column {
            ConversationsTopics(modifier = Modifier, topics, navController = navController)
        }
    }
}

@Composable
fun TopicItem(topic: Topic, modifier: Modifier, onSelectedTopic: (Topic) -> Unit, navController: NavController) {
    Box(modifier = modifier
        .shadow(4.dp, shape = MaterialTheme.shapes.small)
        .fillMaxSize()
        .clip(MaterialTheme.shapes.small)
        .background(if (topic.isSelected) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.inverseOnSurface)
        .padding(16.dp)
        .clickable {
            onSelectedTopic(topic.copy(isSelected = topic.isSelected.not()))
            navController.navigate("${TrainingScreenLabel.TrainingConfiguration.name}?name=${topic.name}&topicId=${topic.id}")
        },
        contentAlignment = Alignment.Center
    ) {
       Text(text = topic.name, color = if (topic.isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun ConversationsTopics(
    modifier: Modifier = Modifier,
    topics: List<Topic>,
    navController: NavController
) {
    val topicListState = remember { TopicDataState() }
    topicListState.setTopicList(topics)

    LazyColumn(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(5.dp)
    ) {
        itemsIndexed(topicListState.topicsList) { index, topic ->
            if (index != 0) Spacer(Modifier.height(2.dp))
            TopicItem(topic, modifier = modifier,
                onSelectedTopic = topicListState::onSelectedTopic,
                navController = navController)
        }
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun HomeScreenPreview() {
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

