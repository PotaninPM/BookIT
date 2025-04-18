package com.prod.bookit.presentation.screens.welcome.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prod.bookit.R

@Composable
fun AuthentificationDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        HorizontalDivider(modifier = Modifier.width(70.dp))
        Text(
            text = stringResource(R.string.or),
            modifier = Modifier.padding(horizontal = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray
        )
        HorizontalDivider(modifier = Modifier.width(70.dp))
    }
}


@Composable
private fun AuthentificationDividerPreview() {
    Surface {
        AuthentificationDivider()
    }
}

@Preview
@Composable
private fun AuthentificationDividerDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        AuthentificationDividerPreview()
    }
}

@Preview
@Composable
private fun AuthentificationDividerLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        AuthentificationDividerPreview()
    }
}