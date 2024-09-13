import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tokitoki.ui.screen.EmailVerificationContents
import com.example.tokitoki.ui.state.EmailVerificationState
import com.example.tokitoki.ui.theme.TokitokiTheme

@Preview(showBackground = true)
@Composable
fun EmailVerificationSnapShotTest() {
    TokitokiTheme {
        EmailVerificationContents()
    }
}

@Preview(showBackground = true)
@Composable
fun EmailVerificationTextFieldSnapShotTest() {
    TokitokiTheme {
        val uiState = EmailVerificationState(code = "1234")
        EmailVerificationContents(uiState = uiState)
    }
}


@Preview(showBackground = true)
@Composable
fun EmailVerificationErrorDialogSnapShotTest() {
    TokitokiTheme {
        val uiState = EmailVerificationState(showDialog = true)
        EmailVerificationContents(uiState = uiState)
    }
}
