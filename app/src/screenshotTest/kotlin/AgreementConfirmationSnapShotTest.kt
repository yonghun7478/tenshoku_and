import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tokitoki.ui.screen.AgreementConfirmationContents
import com.example.tokitoki.ui.state.AgreementConfirmationState
import com.example.tokitoki.ui.theme.TokitokiTheme

@Preview(showBackground = true)
@Composable
fun AgreementConfirmationSnapShotTest() {
    TokitokiTheme {
        AgreementConfirmationContents()
    }
}

@Preview(showBackground = true)
@Composable
fun AgreementConfirmationCheckBoxOffSnapShotTest() {
    TokitokiTheme {
        val uiState = AgreementConfirmationState(isAgeConfirmed = false, isPolicyConfirmed = false)
        AgreementConfirmationContents(uiState = uiState)
    }
}

@Preview(showBackground = true)
@Composable
fun AgreementConfirmationCheckBoxOnSnapShotTest() {
    TokitokiTheme {
        val uiState = AgreementConfirmationState(isAgeConfirmed = true, isPolicyConfirmed = true)
        AgreementConfirmationContents(uiState = uiState)
    }
}

@Preview(showBackground = true)
@Composable
fun AgreementConfirmationDialogSnapShotTest() {
    TokitokiTheme {
        val uiState = AgreementConfirmationState(showDialog = true)
        AgreementConfirmationContents(uiState = uiState)
    }
}




