package com.prod.finaldraftforprod.presentation.screens.booking

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class BookingCanvasParams(
    val cellSize: Dp,
    val paddingSize: Dp
) {

    companion object {
        val default = BookingCanvasParams(
            cellSize = 48.dp,
            paddingSize = 8.dp
        )
    }
}