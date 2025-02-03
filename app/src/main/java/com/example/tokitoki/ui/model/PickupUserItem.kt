package com.example.tokitoki.ui.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import com.example.tokitoki.ui.screen.CardDirection

data class PickupUserItem(
    val id: String,
    val thumbnail: String,
    val age: Int,
    val offset: MutableState<Offset> = mutableStateOf(Offset.Zero),
    val rotation: MutableState<Float> = mutableStateOf(0f),
    val isOut: MutableState<Boolean> = mutableStateOf(false),
    val cardDirection: MutableState<CardDirection> = mutableStateOf(CardDirection.NONE) // 자동 제거 방향 추가
)
