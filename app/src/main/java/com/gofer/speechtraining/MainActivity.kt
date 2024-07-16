package com.gofer.speechtraining

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeechTrainingTheme {
                Home()
            }
        }
    }

    @Composable
    fun Home(modifier: Modifier = Modifier) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Title(
                    Modifier.fillMaxWidth(),
                    name = stringResource(R.string.titleHome)
                )
                // Add a horizontal space between the title and the column
                Spacer(
                    modifier
                        .width(28.dp)
                        .height(16.dp))

                ConversationsTopics(modifier)
            }
        }
    }

    @Composable
    fun Title(modifier: Modifier = Modifier, name: String) {
        Surface(color = MaterialTheme.colorScheme.primary,
            modifier = modifier) {
            Text(
                text = "$name!",
                modifier = modifier,
                color = MaterialTheme.colorScheme.inverseOnSurface,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,

            )
        }
    }

    @Composable
    fun TopicItem(topic: Topic, modifier: Modifier, onSelectedTopic: (Topic) -> Unit) {
        Box(modifier = modifier
            .shadow(4.dp, shape = MaterialTheme.shapes.small)
            .fillMaxSize()
            .clip(MaterialTheme.shapes.small)
            .background(if (topic.isSelected) Color("#9233eb".toColorInt()) else MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .clickable {
                       onSelectedTopic(topic.copy(isSelected = topic.isSelected.not()
                       ))
            },
            contentAlignment = Alignment.Center
        ) {
                Text(text = topic.name, color = if (topic.isSelected) Color.White else Color.Black)
            }
    }

    @Composable
    fun ConversationsTopics(modifier: Modifier = Modifier) {
        val topicListState = remember { TopicDataState() }
        topicListState.setTopicList(topicsList())

        LazyColumn(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(5.dp)) {
            itemsIndexed(topicListState.topicsList) { index, topic ->
                if (index != 0) Spacer(Modifier.height(2.dp))
                TopicItem(topic, modifier = modifier,
                    onSelectedTopic = topicListState::onSelectedTopic)
            }
        }
    }

    @Preview(showBackground = true, name = "Dark Mode")
    @Composable
    private fun GreetingPreview() {
        SpeechTrainingTheme {
           Home()
        }
    }
}