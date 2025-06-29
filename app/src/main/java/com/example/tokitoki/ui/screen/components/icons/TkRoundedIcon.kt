package com.example.tokitoki.ui.screen.components.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tokitoki.R
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.util.DrawableSemantics

@Composable
fun TkRoundedIcon(
    modifier: Modifier = Modifier,
    iconRes: Int = R.drawable.ic_mail,
    size: Dp = 50.dp,
    colors: List<Color> = listOf(LocalColor.current.blue, LocalColor.current.white),
    shape: Shape = CircleShape,
    iconTint: Color = LocalColor.current.white,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(
                brush = Brush.linearGradient(colors = colors),
                shape = shape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = DrawableSemantics.withDrawableId(resId = iconRes),
            painter = painterResource(id = iconRes),
            tint = iconTint,
            contentDescription = ""
        )
    }
}

@Preview
@Composable
fun TkRoundedIconPreview() {
    TokitokiTheme {
        TkRoundedIcon()
    }
}