package com.example.tokitoki.ui.screen.backup

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