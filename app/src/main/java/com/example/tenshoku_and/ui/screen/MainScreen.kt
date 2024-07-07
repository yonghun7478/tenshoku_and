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
        menuListener = viewModel::menuListener
    )
}

@Composable
fun MainContent(
    buttonsData: List<ButtonData> = emptyList(),
    userUiState: UserUiState = UserUiState.Init,
    menuListener: (ButtonData) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        UserMenu(buttonsData = buttonsData, menuListener = menuListener)
        UserList(userUiState = userUiState)
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
fun UserList(
    userUiState: UserUiState = UserUiState.Init,
) {
    LazyColumn {
        if (userUiState is UserUiState.Success) {
            userUiState.users.forEach {
                item {
                    UserItem(it.name, it.email)
                }
            }
        }
    }
}

@Composable
fun UserItem(
    name: String,
    email: String,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val color = LocalColor.current

    Tenshoku_andTheme {
        Surface(
            modifier = modifier
                .height(150.dp)
                .fillMaxWidth()
                .padding(1.dp)/*Padding for surface*/,
            color = color.itemColor,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column {
                Text(text = name, Modifier.padding(spacing.itemPadding)/*Padding for Text*/)
                Text(text = email, Modifier.padding(spacing.itemPadding)/*Padding for Text*/)
            }
        }

        Spacer(modifier = Modifier.height(spacing.itemPadding))
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreView(
    modifier: Modifier = Modifier
) {
    val userList = listOf(
        UserUiModel(
            name = "test1",
            username = "username",
            email = "email",
            phone = "phone",
            website = "website"
        ),
        UserUiModel(
            name = "test1",
            username = "username",
            email = "email",
            phone = "phone",
            website = "website"
        ),
        UserUiModel(
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
            name = "test1",
            username = "username",
            email = "email",
            phone = "phone",
            website = "website"
        ),
        UserUiModel(
            name = "test1",
            username = "username",
            email = "email",
            phone = "phone",
            website = "website"
        ),
        UserUiModel(
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
        UserItem(name = "testtset", email = "john.mckinley@examplepetstore.com")
    }
}