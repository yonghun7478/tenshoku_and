package com.example.tokitoki

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.example.tokitoki.ui.util.DrawableSemantics.DrawableId

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

fun SemanticsNodeInteraction.assertBackgroundColor(tint: Color): SemanticsNodeInteraction {
    val imageBitmap = captureToImage()
    val buffer = IntArray(imageBitmap.width * imageBitmap.height)
    imageBitmap.readPixels(buffer, 0, 0, imageBitmap.width - 1, imageBitmap.height - 1)
    val pixelColors = PixelMap(buffer, 0, 0, imageBitmap.width - 1, imageBitmap.height - 1)

    (0 until imageBitmap.width).forEach { x ->
        (0 until imageBitmap.height).forEach { y ->
            if (pixelColors[x, y] == tint) return this
        }
    }
    throw AssertionError("Assert failed: The component does not contain the color")
}

fun SemanticsNodeInteraction.assertHasDrawable(@DrawableRes id: Int): SemanticsNodeInteraction {
    val semanticsMatcher = SemanticsMatcher.expectValue(DrawableId, id)
    return this.assert(semanticsMatcher)
}