import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tokitoki.ui.screen.RegisterWithEmailContents
import com.example.tokitoki.ui.state.RegisterWithEmailState
import com.example.tokitoki.ui.theme.TokitokiTheme

@Preview(showBackground = true)
@Composable
fun RegisterWithEmailSnapShotTest() {
    TokitokiTheme {
        RegisterWithEmailContents()
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterWithEmailTextFieldSnapShotTest() {
    TokitokiTheme {
        val uiState = RegisterWithEmailState(email = "yonghun@gmail.com")
        RegisterWithEmailContents(uiState = uiState)
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterWithEmailErrorDialogSnapShotTest() {
    TokitokiTheme {
        val uiState = RegisterWithEmailState(showDialog = true)
        RegisterWithEmailContents(uiState = uiState)
    }
}
