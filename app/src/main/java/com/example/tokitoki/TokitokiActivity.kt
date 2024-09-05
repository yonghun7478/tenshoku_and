package com.example.tokitoki

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tokitoki.ui.screen.EmailVerificationScreen
import com.example.tokitoki.ui.screen.TokitokiNavGraph
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.TokitokiViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TokitokiActivity : ComponentActivity() {
    private val viewModel: TokitokiViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition{
                !viewModel.isReady.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd {
                    screen.remove()
                }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd {
                    screen.remove()
                }
                zoomX.start()
                zoomY.start()
            }
        }

//        enableEdgeToEdge()
        setContent {
            TokitokiTheme {
                EmailVerificationScreen()
//                TokitokiNavGraph()
            }
        }
    }
}