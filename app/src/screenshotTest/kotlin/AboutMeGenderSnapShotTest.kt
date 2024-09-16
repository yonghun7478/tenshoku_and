import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tokitoki.ui.screen.AboutMeGenderContents
import com.example.tokitoki.ui.state.AboutMeGenderState
import com.example.tokitoki.ui.state.Gender
import com.example.tokitoki.ui.theme.TokitokiTheme

@Preview(showBackground = true)
@Composable
fun AboutMeGenderSnapShotTest() {
    TokitokiTheme {
        AboutMeGenderContents()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeGenderMaleSnapShotTest() {
    TokitokiTheme {
        val uiState = AboutMeGenderState(selectedGender = Gender.MALE)
        AboutMeGenderContents(uiState = uiState)
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeGenderFemaleSnapShotTest() {
    TokitokiTheme {
        val uiState = AboutMeGenderState(selectedGender = Gender.FEMALE)
        AboutMeGenderContents(uiState = uiState)
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMeGenderDialogSnapShotTest() {
    TokitokiTheme {
        val uiState = AboutMeGenderState(showDialog = true)
        AboutMeGenderContents(uiState = uiState)
    }
}




