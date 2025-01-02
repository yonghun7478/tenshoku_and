package com.example.tokitoki.ui.screen.components.etc

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.TestTags
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme

@Composable
fun <T> TkBottomArrowNavigation(
    modifier: Modifier = Modifier,
    action: (T) -> Unit = {},
    previousActionParam: T,
    nextActionParam: T,
) {
    Row(modifier = modifier) {
        TkBottomArrowNavigationBtn(isNext = false, action = action, actionParam = previousActionParam)
        Spacer(modifier = Modifier.weight(1f))
        TkBottomArrowNavigationBtn(isNext = true, action = action, actionParam = nextActionParam)
    }
}

@Composable
fun <T> TkBottomArrowNavigationBtn(
    isNext: Boolean = false,
    action: (T) -> Unit = {},
    actionParam: T
) {
    Button(
        onClick = { action(actionParam) },
        modifier = Modifier
            .clip(CircleShape)
            .shadow(
                elevation = 10.dp,
                shape = CircleShape
            )
            .size(50.dp)
            .testTag(if (isNext) TestTags.TK_BOTTOM_ARROR_NAVIGATION_NEXT_BTN else TestTags.TK_BOTTOM_ARROR_NAVIGATION_PREVIOUS_BTN),
        colors = ButtonDefaults.buttonColors(containerColor = if (isNext) LocalColor.current.blue else LocalColor.current.lightGray),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = if (isNext) R.drawable.baseline_keyboard_arrow_right_24 else R.drawable.baseline_keyboard_arrow_left_24),
            contentDescription = "Next",
            tint = if (isNext) LocalColor.current.white else LocalColor.current.black,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TkBottomArrowNavigationPreview() {
    TokitokiTheme {
        TkBottomArrowNavigation(
            previousActionParam = "",
            nextActionParam = ""
        )
    }
}