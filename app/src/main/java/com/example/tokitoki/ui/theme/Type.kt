package com.example.tokitoki.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.tokitoki.R

val notosansFontFamily = FontFamily(
    Font(R.font.notosans_jp_black, FontWeight.Black),
    Font(R.font.notosans_jp_bold, FontWeight.Bold),
    Font(R.font.notosans_jp_extra_bold, FontWeight.ExtraBold),
    Font(R.font.notosans_jp_extra_light, FontWeight.ExtraLight),
    Font(R.font.notosans_jp_light, FontWeight.Light),
    Font(R.font.notosans_jp_medium, FontWeight.Medium),
    Font(R.font.notosans_jp_regular, FontWeight.Normal), //
    Font(R.font.notosans_jp_semi_bold, FontWeight.SemiBold),
    Font(R.font.notosans_jp_thin, FontWeight.Thin),
)

// Set of Material typography styles to start with
val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = notosansFontFamily,
//        fontWeight = FontWeight.Bold,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)