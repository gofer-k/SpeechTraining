package com.gofer.speechtraining.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gofer.speechtraining.TrainingScreenLabel
import com.gofer.speechtraining.getDownloadDataIcon
import com.gofer.speechtraining.getUploadDataIcon
import com.gofer.speechtraining.ui.TopBarTitle
import com.gofer.speechtraining.ui.theme.PurpleGrey80

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ConfigScreen(navController: NavController,
                 onExportFile: ((uri: Uri) -> Unit)? = null,
                 onImportFile: ((uri: Uri) -> Unit)? = null) {
  Scaffold(
    topBar = {
      TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = PurpleGrey80),
        title = {
          TopBarTitle(navController = navController,
            TrainingScreenLabel.TrainingAppConfig,
            color = Color.DarkGray)
      })
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center) {
        FilePickerButton(true) { onImportFile?.invoke(it) }
        Spacer(Modifier.height(64.dp))
        FilePickerButton(false) { onExportFile?.invoke(it) }
    }
  }
}

@Composable
fun FilePickerButton(load: Boolean = false, onPickFile: ((fileUri: Uri) -> Unit)?) {
  var pickedUri by remember { mutableStateOf<Uri?>(null) }

  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
  ) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
      pickedUri = result.data?.data
    }
  }

  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Button(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 64.dp, end = 64.dp)
        .align(Alignment.CenterHorizontally),
      onClick = {
        val intent = if (load) {
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
              type = "application/json"
              addCategory(Intent.CATEGORY_OPENABLE)
            }
          } else {
            Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
              type = "application/json"
              putExtra(Intent.EXTRA_TITLE, "export_data.json")
              addCategory(Intent.CATEGORY_OPENABLE)
            }
          }
        launcher.launch(intent)
      }) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        val iconRes by remember { mutableIntStateOf(
          if (load) getUploadDataIcon() else getDownloadDataIcon()
        ) }
        val labelText by remember { mutableIntStateOf(
          if (load) {
            TrainingScreenLabel.TrainingImportAppData.title
          } else {
            TrainingScreenLabel.TrainingExportAppData.title
          }) }

        Icon(
          modifier = Modifier.size(width = 50.dp, height = 50.dp),
          painter = painterResource(id = iconRes),
          contentDescription = null
        )
        Text(
          text = stringResource(id = labelText), fontSize = 20.sp
        )
      }
    }
  }
  onPickFile?.invoke(pickedUri ?: Uri.EMPTY)
}


@Preview(showBackground = true, name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ConfigScreenPreview() {
  val navController = rememberNavController()
  ConfigScreen(navController = navController)
}