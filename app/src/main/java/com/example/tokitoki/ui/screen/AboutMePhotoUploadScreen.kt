package com.example.tokitoki.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.tokitoki.BuildConfig
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AboutMePhotoUploadAction
import com.example.tokitoki.ui.screen.components.buttons.TkBtn
import com.example.tokitoki.ui.screen.components.dialog.TkBottomDialog
import com.example.tokitoki.ui.screen.components.etc.TkIndicator
import com.example.tokitoki.ui.state.AboutMePhotoUploadEvent
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.viewmodel.AboutMePhotoUploadViewModel
import com.example.tokitoki.utils.createImageFile
import java.util.Objects

@Composable
fun AboutMePhotoUploadScreen(
    onAboutMeThirdScreen: () -> Unit = {},
    onAboutMeProfInputScreen: () -> Unit = {},
    viewModel: AboutMePhotoUploadViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            viewModel.updateCapturedImageUri(uri)
            viewModel.updateShowBottomDialogState(false)
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    AboutMePhotoUploadContents(
        showBottomDialog = uiState.showBottomDialog,
        aboutMePhotoUploadAction = viewModel::aboutMePhotoUploadAction,
        capturedImageUri = uiState.capturedImageUri
    )

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AboutMePhotoUploadEvent.ACTION -> {
                    when (event.action) {
                        is AboutMePhotoUploadAction.CLICK_INPUT_BOX -> {
                            if(event.action.hasPicture) {

                            } else {
                                viewModel.updateShowBottomDialogState(true)
                            }
                        }

                        AboutMePhotoUploadAction.NOTHING -> {}
                        AboutMePhotoUploadAction.SUBMIT -> {}
                        AboutMePhotoUploadAction.DISSMISS_BOTTIOM_DIALOG -> {
                            viewModel.updateShowBottomDialogState(false)
                        }

                        AboutMePhotoUploadAction.CLICK_LIBRARY -> {}
                        AboutMePhotoUploadAction.CLICK_TAKE_PICTURE -> {
                            val permissionCheckResult =
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                )
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(uri)
                            } else {
                                // Request a permission
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }

                        }
                    }

                }

                AboutMePhotoUploadEvent.NOTHING -> {}
            }
        }
    }
}

@Composable
fun AboutMePhotoUploadContents(
    showBottomDialog: Boolean = false,
    aboutMePhotoUploadAction: (AboutMePhotoUploadAction) -> Unit = {},
    capturedImageUri: Uri = Uri.EMPTY
) {
    Box {
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
                aboutMePhotoUploadAction = aboutMePhotoUploadAction,
                capturedImageUri = capturedImageUri
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

        TkBottomDialog(
            isVisible = showBottomDialog,
            onDismiss = {
                aboutMePhotoUploadAction(AboutMePhotoUploadAction.DISSMISS_BOTTIOM_DIALOG)
            }, {
                AboutMePhotoUploadBottomDialogContent(
                    aboutMePhotoUploadAction = aboutMePhotoUploadAction
                )
            }
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
    capturedImageUri: Uri = Uri.EMPTY,
    aboutMePhotoUploadAction: (AboutMePhotoUploadAction) -> Unit = {},
) {
    val grayColor = LocalColor.current.lightGray


    if (capturedImageUri.path?.isNotEmpty() == true) {
        Image(
            modifier = modifier
                .fillMaxWidth(0.4f)
                .aspectRatio(0.7f)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    aboutMePhotoUploadAction(AboutMePhotoUploadAction.CLICK_INPUT_BOX(true))
                },
            painter = rememberImagePainter(capturedImageUri),
            contentDescription = null
        )
    } else {
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
                }
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    aboutMePhotoUploadAction(AboutMePhotoUploadAction.CLICK_INPUT_BOX(false))
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
}

@Composable
fun AboutMePhotoUploadBottomDialogContent(
    modifier: Modifier = Modifier,
    aboutMePhotoUploadAction: (AboutMePhotoUploadAction) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.padding(top = 10.dp, start = 5.dp),
            text = "引目写真で雰囲気を伝えましょう",
            fontSize = 13.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        ) {
            AboutMePhotoUploadExampleImg(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp),
                isOk = true
            )
            AboutMePhotoUploadExampleImg(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp),
                isOk = true
            )
            AboutMePhotoUploadExampleImg(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp),
                isOk = false
            )
        }

        Text(
            modifier = Modifier.padding(top = 10.dp, start = 5.dp),
            text = "他の人に取られた写真にしましょう",
            fontSize = 13.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        ) {
            AboutMePhotoUploadExampleImg(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp),
                isOk = true
            )
            AboutMePhotoUploadExampleImg(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp),
                isOk = true
            )
            AboutMePhotoUploadExampleImg(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp),
                isOk = false
            )
        }

        Text(
            modifier = Modifier.padding(top = 10.dp, start = 5.dp),
            text = "*自分以外の人が映っている場合は、モザイクなどでわからないようにしましょう",
            fontSize = 8.sp
        )

        Button(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 5.dp, end = 5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LocalColor.current.lightGray),
            onClick = {
                aboutMePhotoUploadAction(AboutMePhotoUploadAction.CLICK_LIBRARY)
            },
        ) {
            Text(
                text = "ライブラリから選ぶ",
                color = LocalColor.current.black
            )
        }

        Button(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 2.dp, start = 5.dp, end = 5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LocalColor.current.lightGray),
            onClick = {
                aboutMePhotoUploadAction(AboutMePhotoUploadAction.CLICK_TAKE_PICTURE)
            },
        ) {
            Text(
                text = "写真を取る",
                color = LocalColor.current.black
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun AboutMePhotoUploadExampleImg(
    modifier: Modifier = Modifier,
    isOk: Boolean = false
) {
    BoxWithConstraints(
        modifier = modifier
            .padding(5.dp)
    ) {
        // maxWidth를 사용하여 아이콘 크기와 위치를 부모 크기의 비율로 설정
        val iconSize = maxWidth * 0.3f // 예: 부모 너비의 10%
        val iconOffset = maxWidth * -0.05f // 예: 부모 너비의 -5%만큼 오프셋

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.8f)
                .clip(RoundedCornerShape(30.dp))
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = painterResource(id = R.drawable.man_example),
                contentScale = ContentScale.Crop,
                contentDescription = "",
            )
        }

        Box(
            modifier = Modifier
                .size(iconSize) // 퍼센트 기반 크기 적용
                .offset(x = iconOffset, y = iconOffset)
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(LocalColor.current.white)
                .align(alignment = Alignment.TopStart)
        ) {
            Icon(
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .size(iconSize * 0.83f), // 내부 아이콘을 상위 아이콘 크기의 83%로 설정
                painter = painterResource(id = if (isOk) R.drawable.baseline_check_circle_outline_24 else R.drawable.baseline_remove_circle_outline_24),
                tint = if (isOk) LocalColor.current.blue else Color.Red,
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

@Preview(showBackground = true)
@Composable
fun AboutMePhotoUploadBottomDialogContentPreview() {
    TokitokiTheme {
        AboutMePhotoUploadBottomDialogContent()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutMePhotoUploadExampleImgPreview() {
    TokitokiTheme {
        Column {
            AboutMePhotoUploadExampleImg(
                modifier = Modifier.width(120.dp),
                isOk = true
            )
            AboutMePhotoUploadExampleImg(
                modifier = Modifier.width(120.dp)
            )
        }
    }
}