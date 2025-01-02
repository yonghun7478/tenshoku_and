import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tokitoki.ui.screen.SignInContent
import com.example.tokitoki.ui.theme.TokitokiTheme

@Preview(showBackground = true)
@Composable
fun SignInSnapShotTest() {
    TokitokiTheme {
        SignInContent()
    }
}
