package com.example.tokitoki.ui.constants

object AgreementConfirmationConstants {
    const val TAG = "TAG_AGREEMENT_CONFIRMATION"
}

sealed class AgreementConfirmationAction {
    data object NOTHING : AgreementConfirmationAction()
    data object CHECKBOX_AGE_CLICK : AgreementConfirmationAction()
    data object CHECKBOX_POLICY_CLICK : AgreementConfirmationAction()
    data object SUBMIT : AgreementConfirmationAction()
    data object DIALOG_DISMISS : AgreementConfirmationAction()
    data object DIALOG_OK : AgreementConfirmationAction()

}