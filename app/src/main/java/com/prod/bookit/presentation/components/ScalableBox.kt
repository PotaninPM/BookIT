package com.prod.bookit.presentation.components

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun ScalableBox(
    initialScale: Float = 1f,
    minScale: Float = 0.3f,
    maxScale: Float = 3.0f,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.(scale: Float, offset: Offset) -> Unit
) {
    var scale by remember { mutableFloatStateOf(initialScale) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    val newScale = (scale * zoom).coerceIn(minScale, maxScale)
                    val zoomOffset = (centroid - offset) * (1f - zoom)

                    scale = newScale
                    offset += pan + zoomOffset
                }
            },
        contentAlignment = contentAlignment,
        content = { content(scale, offset) }
    )
}