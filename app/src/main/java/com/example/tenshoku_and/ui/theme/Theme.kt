package com.example.tenshoku_and.ui.theme

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

data class LocalSpacingScheme(
    val default: Dp = 8.dp,
)

val LocalSpacing =
    compositionLocalOf<LocalSpacingScheme> { error("No Spacing provided") } // 기본값 없이 정의

data class LocalColorScheme(
    val test: Color = Color(0xFFA1E2EB),
)

val LocalColor =
    compositionLocalOf<LocalColorScheme> { error("No CustomColor provided") } // 기본값 없이 정의


@Composable
fun Tenshoku_andTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    CompositionLocalProvider(
        LocalSpacing provides LocalSpacingScheme(),
        LocalColor provides LocalColorScheme()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}