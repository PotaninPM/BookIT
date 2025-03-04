package com.prod.bookit.presentation.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.prod.bookit.R
import com.prod.bookit.presentation.components.MyTimePicker
import com.prod.bookit.presentation.components.OutlinedBigButton
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun RescheduleDialog(
    onConfirm: (String, String) -> Unit = { _, _ -> },
    onDismiss: () -> Unit = {}
) {
    val date = LocalDate.now()

    val selectedStartTime = remember { mutableStateOf(LocalTime.of(9, 0)) }
    val selectedEndTime = remember { mutableStateOf(LocalTime.of(10, 0)) }

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val zoneId = ZoneId.systemDefault()
    val offset = zoneId.rules.getOffset(LocalDateTime.now())

    fun getIsoStart(): String {
        return LocalDateTime.of(date, selectedStartTime.value)
            .atOffset(offset)
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
    fun getIsoEnd(): String {
        return LocalDateTime.of(date, selectedEndTime.value)
            .atOffset(offset)
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    if (showStartTimePicker) {
        MyTimePicker(
            onTimeSelected = { time ->
                selectedStartTime.value = time
                if (selectedEndTime.value <= time) {
                    selectedEndTime.value = time.plusHours(1)
                }
                showStartTimePicker = false
            },
            onDismiss = { showStartTimePicker = false }
        )
    }
    if (showEndTimePicker) {
        MyTimePicker(
            onTimeSelected = { time ->
                selectedEndTime.value = if (time <= selectedStartTime.value) {
                    selectedStartTime.value.plusHours(1)
                } else {
                    time
                }
                showEndTimePicker = false
            },
            onDismiss = { showEndTimePicker = false }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.Black),
            elevation = CardDefaults.elevatedCardElevation(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.transfer_time),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedBigButton(
                    onClick = { showStartTimePicker = true }
                ) {
                    Text("Начало: ${selectedStartTime.value.format(DateTimeFormatter.ofPattern("HH:mm"))}")
                }
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showEndTimePicker = true },
                    modifier = Modifier.height(48.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text(
                        stringResource(
                            R.string.end,
                            selectedEndTime.value.format(DateTimeFormatter.ofPattern("HH:mm"))
                        ), color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onConfirm(getIsoStart(), getIsoEnd()) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text(stringResource(R.string.choosee), color = Color.White)
                }
            }
        }
    }
}


@Preview
@Composable
private fun RescheduleDialogScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        //RescheduleDialogScreenContent()
    }
}

@Preview
@Composable
private fun RescheduleDialogScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
       // RescheduleDialogScreenContent()
    }
}

