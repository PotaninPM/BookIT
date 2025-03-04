package com.prod.bookit.presentation.screens.booking.test

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.prod.bookit.presentation.components.ScalableBox
import com.prod.bookit.presentation.models.BookObject
import com.prod.bookit.presentation.models.Coworking

@Composable
fun BookingCanvas(
    modifier: Modifier = Modifier,
    coworking: Coworking,
    bookObjects: List<BookObject>,
    bookingCanvasParams: BookingCanvasParams = BookingCanvasParams.default
) {
    ScalableBox(
        modifier = modifier
    ) { scale, offset ->
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                    transformOrigin = TransformOrigin(0f, 0f)
                }
        ) {
            drawRect(
                color = Color.Red
            )
        }
    }
}


@Composable
private fun BookingCanvasPreview() {
//    Surface {
//        BookingCanvas(
//            coworking = Coworking(
//                id = 0,
//                name = "т-ворк",
//                width = 128,
//                height = 256,
//            ),
//            bookObjects = emptyList()
//        )
//    }
}

@Preview
@Composable
private fun BookingCanvasDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        BookingCanvasPreview()
    }
}

@Preview
@Composable
private fun BookingCanvasLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        BookingCanvasPreview()
    }
}