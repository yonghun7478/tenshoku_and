import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tokitoki.ui.screen.AboutMeNameContents
import com.example.tokitoki.ui.state.AboutMeNameState
import com.example.tokitoki.ui.theme.TokitokiTheme

@Preview(showBackground = true)
@Composable
fun AboutMeNameSnapShotTest() {
    TokitokiTheme {
        AboutMeNameContents()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeNameInputSnapShotTest() {
    TokitokiTheme {
        val uiState = AboutMeNameState(name = "yong")
        AboutMeNameContents(uiState = uiState)
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeNameDialogSnapShotTest() {
    TokitokiTheme {
        val uiState = AboutMeNameState(showDialog = true)
        AboutMeNameContents(uiState = uiState)
    }
}




