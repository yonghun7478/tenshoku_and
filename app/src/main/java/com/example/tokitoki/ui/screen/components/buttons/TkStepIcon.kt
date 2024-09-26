package com.example.tokitoki.ui.screen.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tokitoki.ui.theme.LocalColor

@Composable
fun TkStepIcon(
    modifier: Modifier = Modifier,
    stepPos: Int,
    maxStep: Int
) {

    val list = remember(maxStep + 1) { MutableList(maxStep + 1) { false } }
    list[stepPos] = true

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        list.forEachIndexed { index, value ->
            if (index == 0) return@forEachIndexed

            if (value) {
                Text(
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    LocalColor.current.blue,
                                    LocalColor.current.white
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(6.dp),
                    text = "Step${index}",
                    color = LocalColor.current.white,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(15.dp)
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
            }
        }
    }
}