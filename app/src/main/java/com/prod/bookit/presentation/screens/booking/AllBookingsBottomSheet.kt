package com.prod.bookit.presentation.screens.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.prod.bookit.R
import com.prod.bookit.presentation.models.FullBookingInfo
import com.prod.bookit.presentation.viewModels.BookingViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllBookingsBottomSheet(
    vm: BookingViewModel,
    onDismiss: () -> Unit,
    onCancelBooking: (FullBookingInfo) -> Unit = {},
    onTransferClick: (FullBookingInfo) -> Unit,
    onChangeClick: (FullBookingInfo) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var currentPage by remember { mutableIntStateOf(0) }
    var bookings by remember { mutableStateOf<List<FullBookingInfo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var hasMorePages by remember { mutableStateOf(true) }

    val pageSize = 500

    LaunchedEffect(currentPage) {
        isLoading = true
        try {
            val newBookings = vm.getAllCoworkings(page = currentPage, count = pageSize)
            
            if (currentPage == 0) {
                bookings = newBookings
            } else {
                bookings = bookings + newBookings
            }

            hasMorePages = newBookings.size == pageSize
        } catch (e: Exception) {
        } finally {
            isLoading = false
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        AllBookingsBottomSheetContent(
            bookings = bookings,
            isLoading = isLoading,
            hasMorePages = hasMorePages,
            onLoadMore = { currentPage++ },
            onCancelBooking = onCancelBooking,
            onTransferClick = onTransferClick,
            onChangeClick = onChangeClick
        )
    }
}

@Composable
fun AllBookingsBottomSheetContent(
    bookings: List<FullBookingInfo> = emptyList(),
    isLoading: Boolean = false,
    hasMorePages: Boolean = false,
    onLoadMore: () -> Unit = {},
    onTransferClick: (FullBookingInfo) -> Unit,
    onCancelBooking: (FullBookingInfo) -> Unit = {},
    onChangeClick: (FullBookingInfo) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.all_bron),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(bookings.reversed()) { index, booking ->
                    BookingCard(
                        booking = booking,
                        onCancelBooking = { onCancelBooking(booking) },
                        onTransferClick = {
                            onTransferClick(booking)
                        },
                        onChangeClick = {
                            onChangeClick(booking)
                        }
                    )

                    if (index > bookings.size - 3) {
                        LaunchedEffect(Unit) {
                            if (!isLoading && hasMorePages) onLoadMore()
                        }
                    }
                }

                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            if (isLoading && bookings.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun BookingCard(
    booking: FullBookingInfo,
    onCancelBooking: () -> Unit,
    onTransferClick: () -> Unit,
    onChangeClick: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.place_num, booking.position),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_calendar_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = booking.date.format(dateFormatter),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "${booking.timeFrom.format(timeFormatter)} - ${booking.timeUntil.format(timeFormatter)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = booking.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Row {
                    Button(
                        onClick = onCancelBooking,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(text = stringResource(R.string.cancel))
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Button(
                        onClick = onTransferClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(text = stringResource(R.string.transfer))
                    }
                }

                Button(
                    onClick = onChangeClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = stringResource(R.string.edit))
                }
            }
        }
    }
}
