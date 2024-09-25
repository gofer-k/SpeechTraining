
import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
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
import androidx.compose.material3.ProgressIndicatorDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.gofer.speechtraining.Language
import com.gofer.speechtraining.TopicDataState
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.getDefaultTopicIcon
import com.gofer.speechtraining.src.main.model.Topic
import com.gofer.speechtraining.ui.compose.LanguageList
import com.gofer.speechtraining.ui.theme.PurpleGrey80
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
  onFilterTRainingLanguage: (Language) -> Unit) {
  val label = stringResource(TrainingScreenLabel.TrainingLanguage.title)

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
      TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(id = TrainingScreenLabel.TrainingList.title))
            }
        },
        colors = topAppBarColors(containerColor = PurpleGrey80)
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
    Column(modifier = Modifier.padding(paddingValues)) {
      Spacer(modifier = Modifier.height(24.dp))
      ConversationsTopics(navController = navController, topics)
      Text(modifier = Modifier.align(Alignment.CenterHorizontally),
        text = stringResource(id = TrainingScreenLabel.TrainingLanguageLabel.title))
      LanguageList(languages = availableLanguages, viewModelLanguage = selectedLanguage) {
        onFilterTRainingLanguage(it)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalStdlibApi::class)
@Composable
fun TopicItem(navController: NavController,
              topic: Topic,
              onSelectedTopic: (Topic) -> Unit,
              onImageLoading: (Boolean) -> Unit) {
  val navUri = remember {
    "${TrainingScreenLabel.TrainingConfiguration.name}?topicId=${topic.id}"
  }
  val topicUri = remember { topic.imageUri }

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
          navController.navigate(navUri) }) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
          // Build an ImageRequest with Coil
          val listener = object : ImageRequest.Listener {
            override fun onError(request: ImageRequest, result: ErrorResult) {
              super.onError(request, result)
            }

            override fun onSuccess(request: ImageRequest, result: SuccessResult) {
              super.onSuccess(request, result)
            }
          }
          val uriString = topicUri?.toString()
          val placeholder =getDefaultTopicIcon()
          val imageRequest = ImageRequest.Builder(LocalContext.current)
              .data(topicUri)
              .listener(listener)
              .dispatcher(Dispatchers.IO)
              .memoryCacheKey(uriString)
              .diskCacheKey(uriString)
              .placeholder(placeholder)
              .error(placeholder)
              .fallback(placeholder)
              .diskCachePolicy(CachePolicy.ENABLED)
              .memoryCachePolicy(CachePolicy.ENABLED)
              .build()

          Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = topic.name,
            style = TextStyle(
                textIndent = TextIndent(firstLine = 8.sp),
                color = MaterialTheme.colorScheme.primary),
            fontSize = 18.sp,
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

  LaunchedEffect(key1 = loadingCounter) {}

  Box(contentAlignment = Alignment.Center){
    LazyVerticalGrid(
//      modifier = Modifier.alpha(if (loadingCounter.value > 0) 0f else 1f),
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
        .scale(0.25f), strokeWidth = 32.dp,
        color = ProgressIndicatorDefaults.circularColor)
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
              Topic(name = "Fourth")),
          availableLanguages = listOf(),
          selectedLanguage = Language(),
          onFilterTRainingLanguage = {}
        )
    }
}

