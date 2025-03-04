package com.prod.bookit.presentation.models

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.prod.bookit.presentation.util.secondary

object BookObjectDefaults {
    val bookObjectColors
        @Composable get() = BookObjectColors(
            avalibleContainerColor = MaterialTheme.colorScheme.primary,
            avalibleContentColor = MaterialTheme.colorScheme.onPrimary,
            unavalibleContainerColor = MaterialTheme.colorScheme.onSurface.secondary,
            unavalibleContentColor = MaterialTheme.colorScheme.surface.secondary
        )
}