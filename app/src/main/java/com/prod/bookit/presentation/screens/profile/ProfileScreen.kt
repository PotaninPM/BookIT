package com.prod.bookit.presentation.screens.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.prod.bookit.R
import com.prod.bookit.domain.model.ProfileBookingModel
import com.prod.bookit.presentation.components.QrDialog
import com.prod.bookit.presentation.screens.RootNavDestinations
import com.prod.bookit.presentation.viewModels.AuthViewModel
import com.prod.bookit.presentation.viewModels.ProfileViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    rootNavController: NavController
) {
    val profile by viewModel.profile.collectAsState()
    val bookings by viewModel.bookings.collectAsState()
    val isLoaded by viewModel.isLoaded.collectAsState()

    var qrBooking by remember { mutableStateOf<ProfileBookingModel?>(null) }
    var rescheduleBooking by remember { mutableStateOf<ProfileBookingModel?>(null) }

    val activeBookings = bookings.filter { it.status == "active" }
    val pastBookings = bookings.filter { it.status != "active" }

    val pagerState = rememberPagerState(pageCount = { 2 })
    val tabTitles = listOf("Активные брони", "История")

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    var currentTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
        viewModel.loadBookings()
    }

    if (rescheduleBooking != null) {

        RescheduleDialog(
            onDismiss = {
                rescheduleBooking = null
            },
            onConfirm = { timeStart, timeEnd ->
                scope.launch {
                    val response = viewModel.rescheduleBooking(
                        bookingId = rescheduleBooking!!.id,
                        newTimeFrom = timeStart,
                        newTimeUntil = timeEnd
                    )

                    if (response.status == "active") {
                        viewModel.loadBookings()
                        rescheduleBooking = null
                    }
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.clickable { rootNavController.navigateUp() }
                )
                Icon(
                    painter = painterResource(R.drawable.logout_24px),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.clickable {
                        authViewModel.logout()
                        rootNavController.navigate(RootNavDestinations.Welcome) {
                            popUpTo(RootNavDestinations.Welcome) { inclusive = true }
                        }
                    }
                )
            }

            if (profile != null && isLoaded) {
                ProfileHeader(profile = profile!!)

                Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        items(tabTitles) { header ->
                            val index = tabTitles.indexOf(header)
                            Text(
                                text = header,
                                modifier = Modifier.clickable {
                                    currentTab = index
                                    scope.launch { pagerState.animateScrollToPage(index) }
                                },
                                style = if (currentTab == index) {
                                    MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                } else {
                                    MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                        color = Color.Gray
                                    )
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }

                    LaunchedEffect(pagerState.currentPage) {
                        currentTab = pagerState.currentPage
                    }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(top = 12.dp)
                        ) {
                            val bookingsToShow = when (page) {
                                0 -> activeBookings
                                1 -> pastBookings
                                else -> emptyList()
                            }

                            if (bookingsToShow.isEmpty()) {
                                Text(
                                    text = if (page == 0) "Нет активных бронирований" else "Нет прошедших бронирований",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            } else {
                                bookingsToShow.forEach { booking ->
                                    BookingCard(
                                        booking = booking,
                                        onDelete = { viewModel.deleteBooking(booking.id) },
                                        onReschedule = { rescheduleBooking = booking },
                                        onShowQr = { qrBooking = booking }
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
                        }
                    }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally),
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        qrBooking?.let { booking ->
            QrDialog(
                booking = booking,
                onDismiss = { qrBooking = null }
            )
        }

        rescheduleBooking?.let { booking ->
//            RescheduleDialog(
//                booking = booking,
//                onDismiss = { rescheduleBooking = null },
//                onConfirm = { newDate ->
//                    rescheduleBooking = null
//                }
//            )
        }
    }
}

@Preview
@Composable
private fun ProfileScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
       // ProfileScreenContent()
    }
}

@Preview
@Composable
private fun ProfileScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        // ProfileScreenContent()
    }
}

