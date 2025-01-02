package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.AgreementConfirmationAction

sealed class AgreementConfirmationEvent {
    data object NOTHING : AgreementConfirmationEvent()
    data class ACTION(val action: AgreementConfirmationAction) : AgreementConfirmationEvent()
}