package com.example.tokitoki.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

data class TokitokiSpacing(
    val smallPadding: Dp = 2.dp,
    val middlePadding: Dp = 4.dp,
    val largePadding: Dp = 8.dp,
    val extraLargePadding: Dp = 12.dp,
    val superExtraLargePadding: Dp = 16.dp,
)

val LocalSpacing =
    compositionLocalOf<TokitokiSpacing> { error("No Spacing provided") } // 기본값 없이 정의

data class TokitokiColor(
    val grayColor: Color = Color(0xFF3b3b3b),
    val itemColor: Color = Color(0xFFA1E2EB),
    val white: Color = Color(0xFFFFFFFF),
    val black: Color = Color(0xFF000000),
    val blue: Color = Color(0xFF36C2CE),
    val lightGray: Color = Color(0xFFEEEEEE),
    )

val LocalColor =
    compositionLocalOf<TokitokiColor> { error("No CustomColor provided") } // 기본값 없이 정의


@Composable
fun TokitokiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    CompositionLocalProvider(
        LocalSpacing provides TokitokiSpacing(),
        LocalColor provides TokitokiColor()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}