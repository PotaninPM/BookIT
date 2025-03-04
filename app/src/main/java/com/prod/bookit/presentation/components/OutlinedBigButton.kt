package com.prod.bookit.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedBigButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isChosen: Boolean = true,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    border: BorderStroke? = ButtonDefaults.outlinedButtonBorder().copy(
        brush = SolidColor(if (isChosen) MaterialTheme.colorScheme.primary else Color.Gray),
        width = 2.dp
    ),
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = MaterialTheme.shapes.medium,
        enabled = enabled,
        content = content,
        colors = colors,
        border = border
    )
}

@Composable
private fun OutlinedBigButtonPreview() {
    Surface {
        OutlinedBigButton(
            onClick = {},
            isChosen = true
        ) {
            Text("Big button")
        }
    }
}

@Preview
@Composable
private fun OutlinedBigButtonDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        OutlinedBigButtonPreview()
    }
}

@Preview
@Composable
private fun OutlinedBigButtonLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        OutlinedBigButtonPreview()
    }
}