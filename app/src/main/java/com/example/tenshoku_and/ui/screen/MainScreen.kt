package com.example.tenshoku_and.ui.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tenshoku_and.viewmodel.MainViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.sp
import com.example.tenshoku_and.ui.data.ButtonData
import com.example.tenshoku_and.ui.data.ButtonType
import com.example.tenshoku_and.ui.model.UserUiModel
import com.example.tenshoku_and.ui.state.UserUiState
import com.example.tenshoku_and.ui.theme.LocalColor
import com.example.tenshoku_and.ui.theme.LocalSpacing
import com.example.tenshoku_and.ui.theme.Tenshoku_andTheme

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MainContent(
        buttonsData = viewModel.buttonsData,
        userUiState = uiState,
        menuListener = viewModel::menuListener,
        inputListener = viewModel::inputListener,
        onDeleteClick = viewModel::onDeleteClick,
    )
}

@Composable
fun MainContent(
    buttonsData: List<ButtonData> = emptyList(),
    userUiState: UserUiState = UserUiState.Init,
    menuListener: (ButtonData) -> Unit = {},
    inputListener: (Int, String) -> Unit = { _, _ -> },
    onDeleteClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        if (userUiState == UserUiState.Loading) {
            Text(text = "loading", fontSize = 30.sp)
        }
        UserMenu(buttonsData = buttonsData, menuListener = menuListener)
        UserInput(inputListener = inputListener, modifier = Modifier.padding(2.dp))
        UserList(userUiState = userUiState, onDeleteClick = onDeleteClick)
    }
}

@Composable
fun UserMenu(
    buttonsData: List<ButtonData> = emptyList(),
    menuListener: (ButtonData) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3열 Grid
        modifier = modifier.padding(spacing.menuPadding)
    ) {
        items(buttonsData.size) { index ->
            Button(
                onClick = { menuListener(buttonsData[index]) },
                modifier = Modifier
                    .padding(spacing.menuBtnPadding)
                    .fillMaxWidth()
            ) {
                Text(text = buttonsData[index].name)
            }
        }
    }
}

@Composable
fun UserInput(
    inputListener: (Int, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        var idText by remember { mutableStateOf("") }
        var nameText by remember { mutableStateOf("") }


        OutlinedTextField(
            value = idText,
            onValueChange = { idText = it },
            label = { Text("id") },
            modifier = Modifier.weight(1f)
        )

        OutlinedTextField(
            value = nameText,
            onValueChange = { nameText = it },
            label = { Text("name") },
            modifier = Modifier.weight(1f)
        )

        Button(onClick = { inputListener(idText.toInt(), nameText) }) {
            Text("전송")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserInputPreView() {
    Tenshoku_andTheme {
        UserInput()
    }
}

@Composable
fun UserList(
    userUiState: UserUiState = UserUiState.Init,
    onDeleteClick: (Int) -> Unit = {},
) {
    LazyColumn {
        if (userUiState is UserUiState.Success) {
            userUiState.users.forEach {
                item {
                    UserItem(it.id, it.name, it.email, onDeleteClick = onDeleteClick)
                }
            }
        }
    }
}

@Composable
fun UserItem(
    id: Int,
    name: String,
    email: String,
    onDeleteClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var deleteMode by remember { mutableStateOf(false) } // AlertDialog 상태 변수 추가
    var showDialog by remember { mutableStateOf(false) } // AlertDialog 상태 변수 추가

    val spacing = LocalSpacing.current
    val color = LocalColor.current

    Surface(
        modifier = modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(1.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        deleteMode = false
                        showDialog = true // AlertDialog 표시
                    },
                    onLongPress = {
                        deleteMode = true
                        showDialog = true // AlertDialog 표시
                    }
                )
            },
        color = color.itemColor,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column {
            Text(text = name, Modifier.padding(spacing.itemPadding)/*Padding for Text*/)
            Text(text = email, Modifier.padding(spacing.itemPadding)/*Padding for Text*/)
        }
    }

    Spacer(modifier = Modifier.height(spacing.itemPadding))

    if (showDialog) {
        val title = if (deleteMode) "삭제" else "수정"
        val text = if (deleteMode) "삭제하시겠습니까" else "수정하시겠습니까"

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(title) },
            text = { Text(text) },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    if (deleteMode)
                        onDeleteClick(id)
                }) {
                    Text(title)
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreView(
    modifier: Modifier = Modifier
) {
    val userList = listOf(
        UserUiModel(
            id = 1,
            name = "test1",
            username = "username",
            email = "email",
            phone = "phone",
            website = "website"
        ),
        UserUiModel(
            id = 2,
            name = "test1",
            username = "username",
            email = "email",
            phone = "phone",
            website = "website"
        ),
        UserUiModel(
            id = 3,
            name = "test1",
            username = "username",
            email = "email",
            phone = "phone",
            website = "website"
        )
    )
    val userUiState: UserUiState = UserUiState.Success(userList)

    Tenshoku_andTheme {
        var buttons = listOf(
            ButtonData("select", ButtonType.SELECT),
            ButtonData("delete", ButtonType.DELETE),
            ButtonData("update", ButtonType.UPDATE),
            ButtonData("insert", ButtonType.INSERT),
        )
        MainContent(buttonsData = buttons, userUiState = userUiState)
    }
}

@Preview(showBackground = true)
@Composable
fun UserMenuPreView(
    modifier: Modifier = Modifier
) {
    var buttons = listOf(
        ButtonData("select", ButtonType.SELECT),
        ButtonData("delete", ButtonType.DELETE),
        ButtonData("update", ButtonType.UPDATE),
        ButtonData("insert", ButtonType.INSERT),
    )
    Tenshoku_andTheme {
        UserMenu(buttons)
    }
}

@Preview(showBackground = true)
@Composable
fun UserListPreView(
    modifier: Modifier = Modifier
) {
    val userList = listOf(
        UserUiModel(
            id = 1,
            name = "test1",
            username = "username",
            email = "email",
            phone = "phone",
            website = "website"
        ),
        UserUiModel(
            id = 2,
            name = "test1",
            username = "username",
            email = "email",
            phone = "phone",
            website = "website"
        ),
        UserUiModel(
            id = 3,
            name = "test1",
            username = "username",
            email = "email",
            phone = "phone",
            website = "website"
        )
    )
    val userUiState: UserUiState = UserUiState.Success(userList)
    Tenshoku_andTheme {
        UserList(userUiState = userUiState)
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemPreView(
    modifier: Modifier = Modifier
) {
    Tenshoku_andTheme {
        UserItem(id = 1, name = "testtset", email = "john.mckinley@examplepetstore.com")
    }
}