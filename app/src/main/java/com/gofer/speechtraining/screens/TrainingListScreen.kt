
import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.getDefaultTopicIcon
import com.gofer.speechtraining.src.main.model.Topic
import com.gofer.speechtraining.ui.TopicDataState
import com.gofer.speechtraining.ui.compose.Language
import com.gofer.speechtraining.ui.compose.LanguageList
import com.gofer.speechtraining.ui.theme.SpeechTrainingTheme
import kotlinx.coroutines.Dispatchers

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TrainingListsScreen(
  navController: NavController,
  topics: List<Topic>,
  availableLanguages: List<Language>,
  selectedLanguage: Language,
  onFilterTrainingLanguage: (Language) -> Unit,
  onRemoveTopic: (Long) -> Unit
) {
  val bottomBarHeight = remember { mutableStateOf(0f) }
  val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }
  val showBottomBar = remember { mutableStateOf(true) }
  val nestedScrollConnection = remember {
    object : NestedScrollConnection {
      override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y
        val newOffset = bottomBarOffsetHeightPx.value + delta
        bottomBarOffsetHeightPx.value = newOffset.coerceIn(-bottomBarHeight.value, 0f)
        showBottomBar.value = newOffset >= 0f
        return Offset.Zero
      }
    }
  }

  Scaffold(
    modifier = Modifier.nestedScroll(nestedScrollConnection),
    contentWindowInsets = WindowInsets.safeDrawing,
    topBar = {
      val titleRes = stringResource(id = TrainingScreenLabel.TrainingList.title)
      val title = remember { """$titleRes(${topics.size})""" }
      TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                  text = title,
                  color = Color.DarkGray
                )
            }
        },
        actions = {
          IconButton(onClick = {
            navController.navigate("ConfigAppScreen")
          }) {
            Icon(imageVector = Icons.Default.Settings, contentDescription = null)
          }
        }
    )},
    bottomBar = {
      AnimatedVisibility(
        visible = showBottomBar.value,
        enter = slideInVertically(
          initialOffsetY = { it },
          animationSpec = tween(durationMillis = 150)),
        exit = slideOutVertically(
          targetOffsetY = { it },
          animationSpec = tween(durationMillis = 150)) ) {
        BottomAppBar {
          NavigationBarItem(
            modifier = Modifier.onGloballyPositioned { coordinates ->
                bottomBarHeight.value = coordinates.size.height.toFloat()},
            selected = false,
            onClick = {
              navController.navigate("AddTrainingTopic")
            },
            icon = {
              Icon(
                imageVector = Icons.Rounded.AddCircle,
                contentDescription = null,
                Modifier.scale(2.0f)) })
        }
      }
    }
  ) {paddingValues ->
    Column(modifier = Modifier
      .padding(paddingValues)
    ) {
      ConversationsTopics(
        navController = navController,
        topics,
        onRemoveTopic = { onRemoveTopic(it.id) })
      Spacer(modifier = Modifier.height(24.dp))
      Text(modifier = Modifier.align(Alignment.CenterHorizontally),
        text = stringResource(id = TrainingScreenLabel.TrainingLanguageLabel.title))
      LanguageList(languages = availableLanguages, viewModelLanguage = selectedLanguage) { language ->
        onFilterTrainingLanguage(language)

        AppCompatDelegate.setApplicationLocales(
          LocaleListCompat.forLanguageTags(language.locale.language))
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalStdlibApi::class,
  ExperimentalFoundationApi::class
)
@Composable
fun TopicItem(navController: NavController,
              topic: Topic,
              onSelectedTopic: (Topic) -> Unit,
              onMenuClick: (topic: Topic) -> Unit) {
  val navUri = remember {
    "${TrainingScreenLabel.TrainingConfiguration.name}?topicId=${topic.id}"
  }
  val topicUri = remember { topic.imageUri }

  val uriString  = remember { topicUri?.toString() }
  val placeholder = remember { getDefaultTopicIcon() }
  val context = LocalContext.current
  val imageRequest = remember {
    ImageRequest.Builder(context)
      .data(topicUri)
      .crossfade(true)
      .dispatcher(Dispatchers.IO)
      .memoryCacheKey(uriString)
      .diskCacheKey(uriString)
      .placeholder(placeholder)
      .error(placeholder)
      .fallback(placeholder)
      .diskCachePolicy(CachePolicy.ENABLED)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .build()
  }

  val deleteTrainingText = stringResource(id = TrainingScreenLabel.TrainingDelete.title)
  var showMenu by remember { mutableStateOf(false) }
  Card(
        modifier = Modifier
          .size(width = 100.dp, height = 150.dp)
          .padding(8.dp)
          .combinedClickable(
            onClick = {
              onSelectedTopic(topic.copy(isSelected = topic.isSelected.not()))
              navController.navigate(navUri)
            },
            onLongClick = { showMenu = true }
          ),
        colors = CardDefaults.cardColors(),
        elevation =
        CardDefaults.elevatedCardElevation(
            defaultElevation = 12.dp,
            pressedElevation = 18.dp)) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
          Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = topic.name,
            style = TextStyle(
                textIndent = TextIndent(firstLine = 8.sp),
                color = MaterialTheme.colorScheme.primary),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp))
          AsyncImage(
            model = imageRequest,
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = TrainingScreenLabel.TrainingTopicImage.title),
            modifier = Modifier
              .align(Alignment.CenterHorizontally)
              .padding(horizontal = 8.dp, vertical = 4.dp))
          Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp))
       }
      DropdownMenu(
          modifier = Modifier.wrapContentSize(),
          expanded = showMenu,
          onDismissRequest = { showMenu = false }
      ) {
        DropdownMenuItem(
          text = {
            Row {
              Icon(imageVector = Icons.Default.Delete, deleteTrainingText)
              Text(text = "Delete training") }
                 },
          onClick = {
            showMenu = false
            onMenuClick(topic)
          })
      }
  }
}

@Composable
fun ConversationsTopics(navController: NavController, topics: List<Topic>, onRemoveTopic: (topic: Topic) -> Unit) {
  val topicListState = remember { TopicDataState() }
  topicListState.setTopicList(topics)

  Box(modifier = Modifier.height(500.dp)){
    LazyVerticalGrid(
      columns = GridCells.Adaptive(120.dp),
      contentPadding = PaddingValues(16.dp)
    ) {
      itemsIndexed(topicListState.topicsList) { _, topic ->
        TopicItem(
          navController = navController,
          topic,
          onSelectedTopic = topicListState::onSelectedTopic,
          onMenuClick = {
            topicListState.removeTopic(topic)
            onRemoveTopic(topic)
          })
      }
    }
  }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun TrainingListsScreenPreview() {
    SpeechTrainingTheme {
      val navController = rememberNavController()
      TrainingListsScreen(
        navController = navController,
        topics = listOf(
          Topic(name = "First"),
          Topic(name = "Second"),
          Topic(name = "Third"),
          Topic(name = "Fourth"),
          Topic(name = "Fifth"),
          Topic(name = "Sixth"),
          Topic(name = "Seventh"),
          Topic(name = "Eighth"),
          Topic(name = "Ninth")),
        availableLanguages = listOf(),
        selectedLanguage = Language(),
        onFilterTrainingLanguage = {},
        onRemoveTopic = {}
      )
    }
}

