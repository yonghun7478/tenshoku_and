import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tokitoki.ui.screen.AboutMeBirthdayContents
import com.example.tokitoki.ui.state.AboutMeBirthdayState
import com.example.tokitoki.ui.theme.TokitokiTheme

@Preview(showBackground = true)
@Composable
fun AboutMeBirthdaySnapShotTest() {
    TokitokiTheme {
        AboutMeBirthdayContents()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeBirthdayInputSnapShotTest() {
    TokitokiTheme {
        val uiState = AboutMeBirthdayState(birthday = "19911211")
        AboutMeBirthdayContents(uiState = uiState)
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeBirthdayDialogSnapShotTest() {
    TokitokiTheme {
        val uiState = AboutMeBirthdayState(showDialog = true)
        AboutMeBirthdayContents(uiState = uiState)
    }
}




