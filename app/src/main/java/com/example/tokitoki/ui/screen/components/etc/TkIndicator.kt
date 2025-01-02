package com.example.tokitoki.ui.screen.components.etc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme

@Composable
fun TkIndicator(
    modifier: Modifier = Modifier,
    total: Int = 0,
    current: Int = 0,
    circleSize: Int = 6,
    activeCircleSize: Int = 25,
    spaceSize: Int = 5
) {
    var offset = 0f
    var centerPos = 0
    if (total % 2 == 0) {
        centerPos = total / 2
        offset = ((spaceSize / 2) + (circleSize / 2)).toFloat()
    } else {
        centerPos = (total + 1) / 2
    }

    if (current > centerPos) {
        offset += (circleSize + spaceSize) * (centerPos - current).toFloat() - circleSize / 2
    } else if (current < centerPos) {
        offset += (circleSize + spaceSize) * (centerPos - current).toFloat() + circleSize / 2
    }


    Row(
        modifier = modifier.offset(x = offset.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..total) {
            if (i == current) {
                Box(
                    modifier = Modifier
                        .size(activeCircleSize.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    LocalColor.current.blue,
                                    LocalColor.current.white
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(circleSize.dp)
                        .clip(CircleShape)
                        .background(
                            color = LocalColor.current.lightGray,
                            shape = RoundedCornerShape(16.dp)
                        )
                )
            }

            if (i != total)
                Spacer(modifier = Modifier.width(spaceSize.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TkIndicatorPreview() {
    TokitokiTheme {
        TkIndicator(total = 20, current = 1)
    }
}
