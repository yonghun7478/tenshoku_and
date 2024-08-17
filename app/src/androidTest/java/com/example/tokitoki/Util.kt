package com.example.tokitoki

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

fun SemanticsNodeInteraction.assertFontSizeIsEqualTo(expectedSize: TextUnit): SemanticsNodeInteraction {
    val semanticsMatcher = SemanticsMatcher(
        "${SemanticsProperties.Text.name} is fontSize '$expectedSize'"
    ) {
        val textLayoutResults = mutableListOf<TextLayoutResult>()
        it.config.getOrNull(SemanticsActions.GetTextLayoutResult)
            ?.action
            ?.invoke(textLayoutResults)
        return@SemanticsMatcher if (textLayoutResults.isEmpty()) {
            false
        } else {
            textLayoutResults.first().layoutInput.style.fontSize == expectedSize
        }
    }

    return this.assert(semanticsMatcher)
}

fun SemanticsNodeInteraction.assertFontWeightIsEqualTo(fontWeight: FontWeight): SemanticsNodeInteraction {
    val semanticsMatcher = SemanticsMatcher(
        "${SemanticsProperties.Text.name} is fontSize '$fontWeight'"
    ) {
        val textLayoutResults = mutableListOf<TextLayoutResult>()
        it.config.getOrNull(SemanticsActions.GetTextLayoutResult)
            ?.action
            ?.invoke(textLayoutResults)
        return@SemanticsMatcher if (textLayoutResults.isEmpty()) {
            false
        } else {
            textLayoutResults.first().layoutInput.style.fontWeight == fontWeight
        }
    }

    return this.assert(semanticsMatcher)
}

fun SemanticsNodeInteraction.assertFontColorIsEqualTo(color: Color): SemanticsNodeInteraction {
    val semanticsMatcher = SemanticsMatcher(
        "${SemanticsProperties.Text.name} is fontSize '$color'"
    ) {
        val textLayoutResults = mutableListOf<TextLayoutResult>()
        it.config.getOrNull(SemanticsActions.GetTextLayoutResult)
            ?.action
            ?.invoke(textLayoutResults)
        return@SemanticsMatcher if (textLayoutResults.isEmpty()) {
            false
        } else {
            textLayoutResults.first().layoutInput.style.color == color
        }
    }

    return this.assert(semanticsMatcher)
}