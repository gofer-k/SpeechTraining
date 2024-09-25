package com.gofer.speechtraining.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gofer.speechtraining.src.main.model.Phrase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LazyColumnScrollPosition(parentListState: LazyListState = rememberLazyListState(), listPhrases: List<Phrase>) {
  val scope = rememberCoroutineScope()
  val darkMode = isSystemInDarkTheme()
  val textColor = remember { if (darkMode) Color.White else Color.DarkGray }

  Box(modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center) {
    Column(modifier = Modifier
      .padding(4.dp)
      .align(Alignment.TopCenter),
      verticalArrangement = Arrangement.spacedBy(8.dp)) {
      FloatingActionButton(
        onClick = {
          scope.launch {
            parentListState.animateScrollToItem(10)
          }
        },
        shape = RoundedCornerShape(32.dp),
        contentColor = Color.DarkGray,
        elevation = FloatingActionButtonDefaults.elevation(),
        modifier = Modifier.alpha(0.6f)
      ) {
        val firstVisibleIndex = remember {derivedStateOf { parentListState.firstVisibleItemIndex } }
        if (firstVisibleIndex.value >= 0 && listPhrases.isNotEmpty()) {
          Text(
            text = listPhrases.get(firstVisibleIndex.value).name.first().toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = textColor
          )
        }
      }
    }
  }
}


@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LazyColumnScrollPositionPreview() {
  val listState = rememberLazyListState( initialFirstVisibleItemIndex = 0 )
  val phrases = (1.. 20).toList().map { Phrase(it.toString()) }
  LazyColumnScrollPosition(listState, phrases)
}

