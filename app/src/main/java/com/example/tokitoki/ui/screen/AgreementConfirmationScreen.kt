package com.example.tokitoki.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tokitoki.R
import com.example.tokitoki.ui.constants.AgreementConfirmationAction
import com.example.tokitoki.ui.constants.TestTags
import com.example.tokitoki.ui.state.AgreementConfirmationEvent
import com.example.tokitoki.ui.state.AgreementConfirmationState
import com.example.tokitoki.ui.theme.LocalColor
import com.example.tokitoki.ui.theme.TokitokiTheme
import com.example.tokitoki.ui.util.DrawableSemantics
import com.example.tokitoki.ui.viewmodel.AgreementConfirmationViewModel

@Composable
fun AgreementConfirmationScreen(
    onAboutMeScreen: () -> Unit = {},
    viewModel: AgreementConfirmationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AgreementConfirmationContents(
        uiState = uiState,
        agreementConfirmationAction = viewModel::agreementConfirmationAction
    )

    LaunchedEffect(Unit) {
        viewModel.initState()
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                AgreementConfirmationEvent.NOTHING -> {

                }

                is AgreementConfirmationEvent.ACTION -> {
                    when (uiEvent.action) {
                        AgreementConfirmationAction.NOTHING -> {

                        }

                        AgreementConfirmationAction.CHECKBOX_AGE_CLICK -> {
                            viewModel.onAgreementChange(true)
                        }

                        AgreementConfirmationAction.CHECKBOX_POLICY_CLICK -> {
                            viewModel.onAgreementChange(false)
                        }

                        AgreementConfirmationAction.SUBMIT -> {
                            val result = viewModel.validateAgreement()

                            if (result)
                                onAboutMeScreen()
                            else
                                viewModel.updateShowDialogState(true)
                        }

                        AgreementConfirmationAction.DIALOG_DISMISS -> viewModel.updateShowDialogState(
                            false
                        )

                        AgreementConfirmationAction.DIALOG_OK -> viewModel.updateShowDialogState(
                            false
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AgreementConfirmationContents(
    modifier: Modifier = Modifier,
    uiState: AgreementConfirmationState = AgreementConfirmationState(),
    agreementConfirmationAction: (AgreementConfirmationAction) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag(TestTags.AGREEMENT_CONFIRMATION_CONTENTS),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AgreementConfirmationCoupleImages()

        AgreementConfirmationLogo(
            modifier = Modifier.padding(top = 10.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        AgreementConfirmationBottom(
            ageCheckBoxValue = uiState.isAgeConfirmed,
            policyCheckBoxValue = uiState.isPolicyConfirmed,
            agreementConfirmationAction = agreementConfirmationAction
        )
    }

    if (uiState.showDialog) {
        AgreementConfirmationErrorDialog(
            message = stringResource(R.string.validate_agreement_error_msg),
            agreementConfirmationAction = agreementConfirmationAction
        )
    }
}

@Composable
fun AgreementConfirmationCoupleImages(
    modifier: Modifier = Modifier
) {
    Row(modifier.offset(y = 70.dp)) {
        Image(
            painter = painterResource(R.drawable.couple_1),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.couple_2),
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
        )
    }
    Image(
        painter = painterResource(R.drawable.couple_3),
        contentDescription = "avatar",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(130.dp)
            .clip(CircleShape)
    )
}

@Composable
fun AgreementConfirmationLogo(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .offset(x = 10.dp)
                .height(50.dp)
                .then(DrawableSemantics.withDrawableId(resId = R.drawable.pink_heart_logo)),
            painter = painterResource(id = R.drawable.pink_heart_logo),
            contentDescription = "TopLogoImage",
        )

        Text(
            text = stringResource(id = R.string.logo_name),
            color = LocalColor.current.grayColor,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AgreementConfirmationBottom(
    modifier: Modifier = Modifier,
    ageCheckBoxValue: Boolean = false,
    policyCheckBoxValue: Boolean = false,
    agreementConfirmationAction: (AgreementConfirmationAction) -> Unit = {},
) {
    val checkboxColors = CheckboxDefaults.colors(
        checkedColor = LocalColor.current.blue,
        checkmarkColor = LocalColor.current.white,
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically // Row 내 요소 세로 중앙 정렬
        ) {
            Checkbox(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .size(10.dp)
                    .testTag(TestTags.AGREEMENT_CONFIRMATION_AGE_CHECKBOX),
                checked = ageCheckBoxValue,
                onCheckedChange = {
                    agreementConfirmationAction(AgreementConfirmationAction.CHECKBOX_AGE_CLICK)
                },
                colors = checkboxColors
            )
            Text(
                fontSize = 14.sp,
                text = stringResource(R.string.agreement_confirmation_18)
            )
        }
        Row(
            modifier = Modifier.padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically // Row 내 요소 세로 중앙 정렬
        ) {
            Checkbox(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .size(10.dp)
                    .testTag(TestTags.AGREEMENT_CONFIRMATION_POLICY_CHECKBOX),
                checked = policyCheckBoxValue,
                onCheckedChange = {
                    agreementConfirmationAction(AgreementConfirmationAction.CHECKBOX_POLICY_CLICK)
                },
                colors = checkboxColors
            )

            Text(
                fontSize = 14.sp,
                text = stringResource(R.string.agreement_confirmation_policy)
            )
        }
        Text(
            modifier = Modifier.padding(10.dp),
            text = stringResource(R.string.agreement_confirmation_policy_link),
            fontSize = 10.sp,
            color = LocalColor.current.blue
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LocalColor.current.blue),
            onClick = {
                agreementConfirmationAction(AgreementConfirmationAction.SUBMIT)
            }
        ) {
            Text(
                text = stringResource(R.string.agreement_confirmation_submit),
                color = LocalColor.current.white,
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
fun AgreementConfirmationErrorDialog(
    message: String = "",
    agreementConfirmationAction: (AgreementConfirmationAction) -> Unit = {}
) {
    AlertDialog(
        modifier = Modifier,
        onDismissRequest = { agreementConfirmationAction(AgreementConfirmationAction.DIALOG_DISMISS) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                colors = ButtonDefaults.buttonColors(containerColor = LocalColor.current.blue),
                onClick = { agreementConfirmationAction(AgreementConfirmationAction.DIALOG_OK) }
            ) {
                Text(
                    color = LocalColor.current.white,
                    text = stringResource(id = R.string.register_error_dialog_ok)
                )
            }
        },
        containerColor = LocalColor.current.white,
    )
}

@Preview(showBackground = true)
@Composable
fun AgreementConfirmationPreview() {
    TokitokiTheme {
        AgreementConfirmationContents()
    }
}