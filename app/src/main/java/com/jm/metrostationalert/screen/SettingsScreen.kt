package com.jm.metrostationalert.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jm.metrostationalert.datastore.DataStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val dataStore = DataStore(context)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var sliderPosition by rememberSaveable { mutableFloatStateOf(0.0f) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val (title, setTitle) = rememberSaveable {
        mutableStateOf("")
    }
    val (content, setContent) = rememberSaveable {
        mutableStateOf("")
    }
    val padding = Modifier.padding(8.dp)

    LaunchedEffect(Unit) {
        dataStore.getDistance.collect() {
            sliderPosition = it
        }
    }

    LaunchedEffect(Unit) {
        dataStore.getNotiTitle.collect() {
            setTitle(it)
        }
    }

    LaunchedEffect(Unit) {
        dataStore.getNotiContent.collect() {
            setContent(it)
        }
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(text = "설정") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = padding
                .padding(paddingValues)
        ) {
            Text(text = "알림 제목을 설정해 주세요.", modifier = padding)
            Row {
                OutlinedTextField(
                    value = title,
                    onValueChange = setTitle,
                    modifier = padding
                        .weight(0.8f)
                        .height(55.dp)
                )
                Button(
                    onClick = {
                        scope.launch {
                            dataStore.saveNotiTitle(title)
                            scope.launch {
                                snackbarHostState.showSnackbar("저장되었습니다.")
                            }
                        }
                        keyboardController?.hide()
                    },
                    modifier = padding.height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(text = "저장")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "알림 내용을 설정해 주세요.", modifier = padding)
            Row {
                OutlinedTextField(
                    value = content,
                    onValueChange = setContent,
                    modifier = padding
                        .weight(0.8f)
                        .height(55.dp)
                )
                Button(
                    onClick = {
                        scope.launch {
                            dataStore.saveNotiContent(content)
                            scope.launch {
                                snackbarHostState.showSnackbar("저장되었습니다.")
                            }
                        }
                        keyboardController?.hide()
                    },
                    modifier = padding.height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(text = "저장")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "역 도착 몇 km 전에 알림을 받으실 건가요?", modifier = padding)
            Button(
                onClick = { showBottomSheet = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                modifier = padding.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text(text = String.format("%.1f", sliderPosition) + "km", fontSize = 20.sp)
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row {
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { showBottomSheet = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        }
                        Slider(
                            value = sliderPosition,
                            onValueChange = { sliderPosition = it },
                            steps = 8,
                            valueRange = 0.5f..5.0f
                        )
                        Text(text = String.format("%.1f", sliderPosition) + "km", fontSize = 26.sp)
                        Spacer(modifier = Modifier.height(50.dp))
                        Button(
                            onClick = {
                                showBottomSheet = false
                                scope.launch {
                                    dataStore.saveDistance(sliderPosition)
                                    snackbarHostState.showSnackbar("저장되었습니다.")
                                }
                            },
                            modifier = padding.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Text(text = "확인")
                        }
                        Spacer(modifier = Modifier.height(50.dp))

                    }
                }
            }
            BannersAds()
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}