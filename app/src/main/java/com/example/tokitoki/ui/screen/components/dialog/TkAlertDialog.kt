package com.example.tokitoki.ui.screen.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tokitoki.R
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme

@Composable
fun TkAlertDialog(
    modifier: Modifier = Modifier,
    message: String = "",
    onDismissRequest: () -> Unit = {},  // 다이얼로그 닫힘 처리
    confirmButtonText: String = stringResource(id = R.string.register_error_dialog_ok),  // 확인 버튼 텍스트
    confirmButtonColor: Color = LocalColor.current.blue,  // 확인 버튼 색상
    confirmButtonTextColor: Color = LocalColor.current.white,  // 확인 버튼 텍스트 색상
    onConfirmButtonClick: () -> Unit = { onDismissRequest() },  // 확인 버튼 클릭 시 액션
    containerColor: Color = LocalColor.current.white,  // 다이얼로그 배경 색상
    textColor: Color = LocalColor.current.black  // 메시지 텍스트 색상
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismissRequest() },  // 다이얼로그 닫기 처리
        text = {
            Text(
                text = message,
                color = textColor
            )
        },
        confirmButton = {
            TextButton(
                colors = ButtonDefaults.buttonColors(containerColor = confirmButtonColor),
                onClick = { onConfirmButtonClick() }
            ) {
                Text(
                    color = confirmButtonTextColor,
                    text = confirmButtonText
                )
            }
        },
        containerColor = containerColor,
    )
}

@Preview
@Composable
fun TkAlertDialogPreview() {
    TokitokiTheme {
        TkAlertDialog()
    }
}
