package com.example.tokitoki.utils

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.Credential
import com.example.tokitoki.domain.model.CredentialType
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import javax.inject.Inject

class GoogleSignInManager @Inject constructor(
    val context: Context
) {
    suspend fun requestLogin(activityContext: Context): ResultWrapper<Credential> {
        val signInWithGoogleOption: GetSignInWithGoogleOption =
            GetSignInWithGoogleOption.Builder("945331839593-qlm9du6m229fpi11pk1ootiroaephd1b.apps.googleusercontent.com")
                .setNonce("nonce")
                .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        val credentialManager = CredentialManager.create(context)

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = activityContext,
            )

            val credential = result.credential

            when (credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        try {
                            // Use googleIdTokenCredential and extract id to validate and
                            // authenticate on your server.
                            val googleIdTokenCredential = GoogleIdTokenCredential
                                .createFrom(credential.data)

                            return ResultWrapper.Success(
                                Credential(
                                    id = googleIdTokenCredential.id,
                                    token = googleIdTokenCredential.idToken,
                                    type = CredentialType.GOOGLE_ID_TOKEN
                                )
                            )

                        } catch (e: GoogleIdTokenParsingException) {
                            Log.e("TAG", "Received an invalid google id token response", e)
                        }
                    }
                }

                else -> {
                    // Catch any unrecognized credential type her.
                    Log.e("TAG", "Unexpected type of credential")
                }
            }


        } catch (e: GetCredentialException) {
//            handleFailure(e)
        }

        return ResultWrapper.Error(errorType = ResultWrapper.ErrorType.ExceptionError(message = "message"))
    }
}