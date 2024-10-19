package com.example.tokitoki.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMeInterestAction
import com.example.tokitoki.ui.constants.AboutMePhotoUploadAction
import com.example.tokitoki.ui.constants.AgreementConfirmationAction
import com.example.tokitoki.ui.screen.components.buttons.TkBtn
import com.example.tokitoki.ui.screen.components.etc.TkIndicator
import com.example.tokitoki.ui.state.AboutMePhotoUploadEvent
import com.example.tokitoki.ui.state.Gender
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.util.DrawableSemantics
import com.example.tokitoki.ui.viewmodel.AboutMePhotoUploadViewModel
import kotlinx.coroutines.launch

@Composable
fun AboutMePhotoUploadScreen(
    onAboutMeThirdScreen: () -> Unit = {},
    onAboutMeProfInputScreen: () -> Unit = {},
    viewModel: AboutMePhotoUploadViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

        }

    AboutMePhotoUploadContents(
        aboutMePhotoUploadAction = viewModel::aboutMePhotoUploadAction
    )


    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AboutMePhotoUploadEvent.ACTION -> {
                    when (event.action) {
                        AboutMePhotoUploadAction.CLICK_INPUT_BOX -> {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }

                        AboutMePhotoUploadAction.NOTHING -> {}
                        AboutMePhotoUploadAction.SUBMIT -> {}
                    }

                }

                AboutMePhotoUploadEvent.NOTHING -> {}
            }
        }
    }
}

@Composable
fun AboutMePhotoUploadContents(
    aboutMePhotoUploadAction: (AboutMePhotoUploadAction) -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TkIndicator(
            modifier = Modifier.padding(top = 30.dp),
            total = 20,
            current = 19
        )

        AboutMePhotoUploadTitle(
            modifier = Modifier.padding(top = 10.dp),
        )

        AboutMePhotoUploadInputBox(
            modifier = Modifier.padding(top = 40.dp),
            aboutMePhotoUploadAction = aboutMePhotoUploadAction
        )

        Spacer(modifier = Modifier.weight(1f))

        TkBtn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            text = stringResource(R.string.agreement_confirmation_submit),
            textColor = LocalColor.current.white,
            backgroundColor = LocalColor.current.blue,
            action = aboutMePhotoUploadAction,
            actionParam = AboutMePhotoUploadAction.SUBMIT
        )
    }

}

@Composable
fun AboutMePhotoUploadTitle(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.about_me_photo_upload_title),
        color = LocalColor.current.black,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
fun AboutMePhotoUploadInputBox(
    modifier: Modifier = Modifier,
    aboutMePhotoUploadAction: (AboutMePhotoUploadAction) -> Unit = {},
    ) {
    val grayColor = LocalColor.current.lightGray

    Box(
        modifier = modifier
            .fillMaxWidth(0.4f)
            .aspectRatio(0.7f)
            .drawWithContent {
                drawContent()

                val strokeWidth = 2.dp.toPx()
                val dashLength = 5.dp.toPx()
                val gapLength = 5.dp.toPx()

                drawRoundRect(
                    color = grayColor, // 원하는 테두리 색상
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(dashLength, gapLength), 0f
                        )
                    ),
                    cornerRadius = CornerRadius.Zero // 원하는 경우 모서리 둥글기 설정 가능
                )
            }.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                aboutMePhotoUploadAction(AboutMePhotoUploadAction.CLICK_INPUT_BOX)
            }
    ) {
        Column(
            modifier = Modifier.align(alignment = Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.about_me_photo_upload_input_title),
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                lineHeight = 13.sp,
            )

            Spacer(modifier = Modifier.height(5.dp))

            Icon(
                modifier = modifier
                    .clip(CircleShape)
                    .background(color = LocalColor.current.blue),
                painter = painterResource(id = R.drawable.baseline_add_24),
                tint = LocalColor.current.white,
                contentDescription = ""
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AboutMePhotoUploadContentsPreview() {
    TokitokiTheme {
        AboutMePhotoUploadContents()
    }
}