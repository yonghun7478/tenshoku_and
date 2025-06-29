package com.example.tokitoki.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.ui.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    onNavigateToMain: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val isTokenExist by viewModel.isTokenExist.collectAsState()
    val isCheckComplete by viewModel.isCheckComplete.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkToken()
    }

    LaunchedEffect(isTokenExist, isCheckComplete) {
        if (isCheckComplete) {
            if (isTokenExist) {
                onNavigateToMain()
            } else {
                onNavigateToSignIn()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "로딩 중...") // 실제 스플래시 화면에 표시될 내용을 여기에 추가
    }
} 