package com.example.tokitoki.ui.screen.mypage

import androidx.compose.ui.graphics.vector.ImageVector

data class MyPageListItemData( // 새로운 데이터 클래스
    val text: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)
