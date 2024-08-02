package com.example.tenshoku_and.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SecondScreen(name: String, backAction: () -> Unit) {
    Text(text = name)

    BackHandler(enabled = true) {
        backAction()
    }
}