import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tokitoki.ui.screen.AboutMePhotoUploadBottomDialogContent
import com.example.tokitoki.ui.screen.AboutMePhotoUploadContents
import com.example.tokitoki.ui.theme.TokitokiTheme

@Preview(showBackground = true)
@Composable
fun AboutMePhotoUploadSnapShotTest() {
    TokitokiTheme {
        AboutMePhotoUploadContents()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMePhotoUploadDialogContentsSnapShotTest() {
    TokitokiTheme {
        AboutMePhotoUploadBottomDialogContent(
            showDeleteBtn = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMePhotoUploadDialogContentsShowDeleteBtnSnapShotTest() {
    TokitokiTheme {
        AboutMePhotoUploadBottomDialogContent(
            showDeleteBtn = true
        )
    }
}

