package com.example.tenshoku_and.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tenshoku_and.viewmodel.MainViewModel
import androidx.compose.ui.Modifier
import com.example.tenshoku_and.ui.model.UserUiModel
import com.example.tenshoku_and.ui.state.UserUiState
import com.example.tenshoku_and.ui.theme.Tenshoku_andTheme

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var userlist = emptyList<UserUiModel>()
    if (uiState is UserUiState.Success) {
        userlist = (uiState as UserUiState.Success).users
    }

    MainContent(
        buttons = viewModel.buttons, userlist, clickButton = viewModel::clickButton
    )
}

@Composable
fun MainContent(
    buttons: List<String> = emptyList(),
    userLists: List<UserUiModel> = emptyList(),
    clickButton: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
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
