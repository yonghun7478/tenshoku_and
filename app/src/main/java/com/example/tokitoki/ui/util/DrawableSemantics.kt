package com.example.tokitoki.ui.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics

object DrawableSemantics {
    val DrawableId = SemanticsPropertyKey<Int>("DrawableResId")
    var SemanticsPropertyReceiver.drawableId by DrawableId

    fun withDrawableId(resId: Int) = Modifier.semantics { drawableId = resId }
}