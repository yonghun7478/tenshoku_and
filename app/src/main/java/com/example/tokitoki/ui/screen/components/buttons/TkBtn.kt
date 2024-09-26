package com.example.tokitoki.ui.screen.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.SignInAction
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.util.DrawableSemantics

@Composable
fun <T> TkBtn(
    modifier: Modifier = Modifier,
    text: String = "",
    textColor: Color = LocalColor.current.black,
    backgroundColor: Color = LocalColor.current.white,
    iconRes: Int = -1,
    action: (T) -> Unit = {},
    actionParam: T
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        onClick = {
            action(actionParam)
        },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if(iconRes != -1) {
                Icon(
                    modifier = DrawableSemantics.withDrawableId(resId = iconRes),
                    painter = painterResource(id = iconRes),
                    tint = textColor,
                    contentDescription = "SignMenuBtnIcon"
                )
                Text(text = text, color = textColor, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(1.dp))
            } else {
                Spacer(modifier = Modifier.width(1.dp))
                Text(text = text, color = textColor, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(1.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TkBtnPreView() {
    TokitokiTheme {
        TkBtn(
            text = "メールアドレスでサインイン",
            textColor = LocalColor.current.white,
            backgroundColor = LocalColor.current.blue,
            iconRes = R.drawable.ic_mail,
            actionParam = SignInAction.Nothing
        )
    }
}