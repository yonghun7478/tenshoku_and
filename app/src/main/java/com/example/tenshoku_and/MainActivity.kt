package com.example.tenshoku_and

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tenshoku_and.ui.screen.TenshokuAndNavGraph
import com.example.tenshoku_and.ui.theme.Tenshoku_andTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tenshoku_andTheme {
                TenshokuAndNavGraph()
            }
        }
    }
}