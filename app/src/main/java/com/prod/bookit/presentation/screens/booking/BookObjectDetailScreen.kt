package com.prod.bookit.presentation.screens.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prod.bookit.R
import com.prod.bookit.presentation.components.BigButton
import com.prod.bookit.presentation.components.OutlinedBigButton
import com.prod.bookit.presentation.models.BookingData
import com.prod.bookit.presentation.models.BookingStatus
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookObjectDetailScreen(
    bookingStatus: BookingStatus,
    onDismiss: () -> Unit,
    bookingData: BookingData,
    onBookClick: (BookingData) -> Unit = {}
) {
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        BookObjectDetailScreenContent(
            bookingData = bookingData,
            onBookClick = { onBookClick(if (it == null) bookingData else bookingData.copy(spotId = it)) },
            onChangeClick = onDismiss,
            bookingStatus = bookingStatus
        )
    }
}

@Composable
private fun BookObjectDetailScreenContent(
    bookingStatus: BookingStatus,
    bookingData: BookingData,
    onChangeClick: () -> Unit = {},
    onBookClick: (altSpotId: String?) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.booking__coworking_title, bookingData.coworkingName),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.booking__place_title, bookingData.bookObjectPosition),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.info),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.booking__date, bookingData.date.format(
                        DateTimeFormatter.ofPattern("dd MMMM")
                    )),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(
                        R.string.booking__from_until,
                        bookingData.startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                        bookingData.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        when (bookingStatus) {
            is BookingStatus.Error-> {
                if (bookingStatus.bookObjectUIData == null) {
                    Text(
                        text = stringResource(R.string.error_bron_exist),
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Column {
                        Text(
                            text = stringResource(
                                R.string.ups_bron_gg,
                                bookingStatus.bookObjectUIData.position
                            ),
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        BigButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onBookClick(bookingStatus.bookObjectUIData.id) }
                        ) {
                            Text(
                                stringResource(
                                    R.string.bron_place,
                                    bookingStatus.bookObjectUIData.position
                                ))
                        }
                    }
                }
            }

            is BookingStatus.Success -> Text(
                text = stringResource(R.string.suc_bron),
                textAlign = TextAlign.Center
            )

            else -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            OutlinedBigButton(
                modifier = Modifier.weight(1f),
                onClick = onChangeClick
            ) {
                Text(
                    text = stringResource(R.string.booking__change)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            BigButton(
                modifier = Modifier.weight(1f),
                enabled = bookingStatus is BookingStatus.Empty,
                onClick = { onBookClick(null) }
            ) {
                when (bookingStatus) {
                    is BookingStatus.Loading -> CircularProgressIndicator(
                        modifier = Modifier.size(24.dp)
                    )

                    else -> Text(
                        text = stringResource(R.string.booking__book)
                    )
                }
            }
        }
    }
}


@Composable
private fun BookObjectDetailScreenPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        BookObjectDetailScreenContent(
            bookingStatus = BookingStatus.Success,
            BookingData(
                spotId = "",
                coworkingName = "т-ворк",
                bookObjectPosition = 26,
                startTime = LocalTime.of(16, 30),
                endTime = LocalTime.of(18, 0),
                date = LocalDate.of(2025, 3, 12)
            )
        )
    }
}

@Preview
@Composable
private fun BookObjectDetailScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        BookObjectDetailScreenPreview()
    }
}

@Preview
@Composable
private fun BookObjectDetailScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        BookObjectDetailScreenPreview()
    }
}