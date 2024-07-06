package com.example.tenshoku_and.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tenshoku_and.viewmodel.MainViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.tenshoku_and.ui.state.UserUiState
import com.example.tenshoku_and.ui.theme.Tenshoku_andTheme

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MainContent(
        buttons = viewModel.buttons, uiState, clickButton = viewModel::clickButton
    )
}

@Composable
fun MainContent(
    buttons: List<String> = emptyList(),
    userUiState: UserUiState = UserUiState.Init,
    clickButton: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // 3열 Grid
            modifier = Modifier.padding(8.dp)
        ) {
            items(buttons.size) { index ->
                Button(
                    onClick = { clickButton(buttons[index]) },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = buttons[index])
                }
            }
        }

        LazyColumn {
            if (userUiState is UserUiState.Success) {
                userUiState.users.forEach {
                    item {
                        Surface(
                            modifier = Modifier
                                .height(150.dp)
                                .width(250.dp)
                                .padding(1.dp)/*Padding for surface*/,
                            color = Color(0xFFA1E2EB),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column {
                                Text(text = it.name, Modifier.padding(16.dp)/*Padding for Text*/)
                                Text(text = it.email, Modifier.padding(16.dp)/*Padding for Text*/)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreView(
    modifier: Modifier = Modifier
) {
    Tenshoku_andTheme {
        var buttons = listOf("select", "delete", "update", "insert")

        MainContent(buttons)
    }
}
