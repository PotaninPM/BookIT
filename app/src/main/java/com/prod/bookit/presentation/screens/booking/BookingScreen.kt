package com.prod.bookit.presentation.screens.booking

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.prod.bookit.R
import com.prod.bookit.domain.model.BookObjectUIData
import com.prod.bookit.presentation.components.BigButton
import com.prod.bookit.presentation.components.MyDatePicker
import com.prod.bookit.presentation.components.MyTimePicker
import com.prod.bookit.presentation.components.OutlinedBigButton
import com.prod.bookit.presentation.components.ScalableBox
import com.prod.bookit.presentation.models.BookingData
import com.prod.bookit.presentation.models.BookingStatus
import com.prod.bookit.presentation.models.Coworking
import com.prod.bookit.presentation.models.CoworkingDefaults
import com.prod.bookit.presentation.models.FullBookingInfo
import com.prod.bookit.presentation.screens.RootNavDestinations
import com.prod.bookit.presentation.screens.booking.shemes.ShemeType1
import com.prod.bookit.presentation.screens.profile.RescheduleDialog
import com.prod.bookit.presentation.state.AuthState
import com.prod.bookit.presentation.theme.DarkBlueTheme
import com.prod.bookit.presentation.theme.LightBlueTheme
import com.prod.bookit.presentation.util.secondary
import com.prod.bookit.presentation.viewModels.AuthViewModel
import com.prod.bookit.presentation.viewModels.BookingViewModel
import com.prod.bookit.presentation.viewModels.ProfileViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun BookingScreen(
    rootNavController: NavController,
    coworking: Coworking,
    vm: BookingViewModel = getKoin().get(),
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    var bookingStatus by remember { mutableStateOf<BookingStatus>(BookingStatus.Empty) }
    var bookingObjects by remember { mutableStateOf(
        List(28) { BookObjectUIData(
            id = it.toString(),
            position = it + 1,
            avalibleToBook = false
        ) }
    ) }

    val isAdmin = profileViewModel.profile.collectAsState().value?.isBusiness ?: false

    var spotId by remember { mutableStateOf<String?>(null) }

    var allBookingsBottomSheetOpened by remember { mutableStateOf(false) }

    var openTransferDialog by remember { mutableStateOf<FullBookingInfo?>(null) }
    var changeUserInfo by remember { mutableStateOf<FullBookingInfo?>(null) }

    if (openTransferDialog != null) {
        RescheduleDialog(
            onConfirm = { start, end ->
                coroutineScope.launch {
                    profileViewModel.rescheduleBooking(
                        bookingId = openTransferDialog!!.id,
                        newTimeFrom = start,
                        newTimeUntil = end
                    )

                    openTransferDialog = null
                }
            },
            onDismiss = {
                openTransferDialog = null
            }
        )
    }

    if (changeUserInfo != null) {
        ChangeUserInfoDialog(
            userId = changeUserInfo!!.userId,
            currentEmail = changeUserInfo!!.email,
            currentFullName = changeUserInfo!!.name,
            onDismiss = {
                changeUserInfo = null
            },
            onConfirm = { email, name, id ->
                profileViewModel.changeUserInfo(changeUserInfo!!.userId, email, name)
                changeUserInfo = null
            }
        )
    }

    LaunchedEffect(Unit) {
        bookingObjects = vm.getSpotsForCoworking(
            coworkingId = coworking.id,
            timeFrom = LocalTime.of(16, 0),
            timeUntil = LocalTime.of(18, 0),
            date = LocalDate.now()
        )
    }

    BookingScreenContent(
        isAdmin = isAdmin,
        coworking = coworking,
        onBackClick = {
            rootNavController.navigate(RootNavDestinations.Coworkings) {

            }
        },
        onInfoClick = {
            rootNavController.navigate(RootNavDestinations.Profile) {

            }
        },
        onBookClick = { it, f ->
            coroutineScope.launch {
                vm.book(it).collectLatest { status ->
                    bookingStatus = status
                }

                f()
            }
        },
        bookingStatus = bookingStatus,
        refreshBookingStatus = { bookingStatus = BookingStatus.Empty },
        bookObjects = bookingObjects,
        updateSpots = { startTime, endTime, date ->
            coroutineScope.launch {
                bookingObjects = vm.getSpotsForCoworking(
                    coworkingId = coworking.id,
                    timeFrom = startTime,
                    timeUntil = endTime,
                    date = date
                )
            }
        },
        openBookingDetails = {
            spotId = it.id
        },
        onBookingsListClick = {
            allBookingsBottomSheetOpened = true
        },
        onScanQrClicked = {
            rootNavController.navigate(RootNavDestinations.QrCode)
        }
    )

    spotId?.let {
        BookingDetails(
            spotId = it,
            vm = vm,
            onDismiss = { spotId = null }
        )
    }

    if (allBookingsBottomSheetOpened) {
        AllBookingsBottomSheet(
            vm = vm,
            onDismiss = { allBookingsBottomSheetOpened = false },
            onCancelBooking = { booking ->
                allBookingsBottomSheetOpened = false
                coroutineScope.launch {
                    vm.cancelBooking(booking.id)
                }
            },
            onTransferClick = { it ->
                coroutineScope.launch {
                    openTransferDialog = it
                }
            },
            onChangeClick = { it ->
                changeUserInfo = it
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookingScreenContent(
    openBookingDetails: (BookObjectUIData) -> Unit = {},
    isAdmin: Boolean = false,
    coworking: Coworking,
    bookingStatus: BookingStatus,
    onBookingsListClick: () -> Unit = {},
    onScanQrClicked: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onInfoClick: () -> Unit = {},
    bookObjects: List<BookObjectUIData> = List(28) { BookObjectUIData(
        id = it.toString(),
        position = it + 1,
        avalibleToBook = false
    ) },
    onBookClick: (BookingData, () -> Unit) -> Unit = { _, _ -> },
    refreshBookingStatus: () -> Unit = {},
    updateSpots: (startTime: LocalTime, endTime: LocalTime, date: LocalDate) -> Unit = { _, _, _ -> }
) {
    val initialTime = LocalTime.now().plusMinutes(10)

    var startTime by remember { mutableStateOf<LocalTime>(initialTime) }
    var endTime by remember { mutableStateOf<LocalTime>(initialTime.plusHours(1)) }
    var date by remember { mutableStateOf<LocalDate>(LocalDate.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            updateSpots(startTime, endTime, date)
            delay(1500)
        }
    }

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    var isChosenBtn by remember { mutableIntStateOf(1) }

    var bookingData by remember { mutableStateOf<BookingData?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
             CenterAlignedTopAppBar(
                 navigationIcon = {
                     IconButton(onClick = onBackClick) {
                         Icon(
                             painter = painterResource(R.drawable.ic_list_24),
                             contentDescription = null
                         )
                     }
                 },
                 title = {
                     Text(
                         text = stringResource(
                             R.string.booking__coworking_title, coworking.name
                         ),
                         modifier = Modifier.basicMarquee(Int.MAX_VALUE)
                     )
                 },
                 actions = {
                     IconButton(onClick = onInfoClick) {
                         Icon(
                             painter = painterResource(R.drawable.ic_account_circle_24),
                             contentDescription = null
                         )
                     }
                 }
             )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .zIndex(10f)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(MaterialTheme.colorScheme.primary)
                        )

                        Text(stringResource(R.string.free))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(
                                    MaterialTheme.colorScheme.onSurface.secondary
                                )
                        )

                        Text(stringResource(R.string.zaniato))
                    }
                }

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    ScalableBox(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(unbounded = true, align = Alignment.TopStart),
                        contentAlignment = Alignment.TopStart,
                        initialScale = maxWidth / ((CoworkingDefaults.cellSize * 2 + CoworkingDefaults.spaceSize + 48.dp + CoworkingDefaults.wallWidth) * 2 + 128.dp)
                    ) { scale, offset ->
                        if (bookObjects.isNotEmpty()) {
                            ShemeType1(
                                isAdmin = false,
                                bookObjects = bookObjects,
                                onBookObjectClick = {
                                    refreshBookingStatus()

                                    if (it.avalibleToBook) {
                                        bookingData = BookingData(
                                            spotId = it.id,
                                            coworkingName = coworking.name,
                                            bookObjectPosition = it.position,
                                            startTime = startTime,
                                            endTime = endTime,
                                            date = date
                                        )
                                    } else {
                                        openBookingDetails(it)
                                    }
                                },
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                        translationX = offset.x
                                        translationY = offset.y
                                        transformOrigin = TransformOrigin(0f, 0f)
                                    }
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.surface
                                    )
                                )
                            )
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .align(Alignment.TopCenter)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.surface,
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(4.dp)
                            .align(Alignment.CenterStart)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.surface,
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(4.dp)
                            .align(Alignment.CenterEnd)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.surface
                                    )
                                )
                            )
                    )

                }

                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(vertical = 16.dp)
                ) {
                    Row {
                        OutlinedBigButton(
                            modifier = Modifier.weight(1f),
                            onClick = { showStartTimePicker = true }
                        ) {
                            Text(stringResource(
                                R.string.booking__from,
                                startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                            ))
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedBigButton(
                            modifier = Modifier.weight(1f),
                            onClick = { showEndTimePicker = true }
                        ) {
                            Text(stringResource(
                                R.string.booking__until,
                                endTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                            ))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isAdmin) Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BigButton(
                            modifier = Modifier
                                .weight(1f),
                            onClick = onBookingsListClick,
                            isChosen = isChosenBtn == 1
                        ) {
                            Text(stringResource(R.string.list_brons))
                        }

                        BigButton(
                            modifier = Modifier.weight(1f),
                            onClick = onScanQrClicked
                        ) {
                            Text(stringResource(R.string.scan_qr))
                        }
                    }

                    if (!isAdmin) Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedBigButton(
                            onClick = {
                                showDatePicker = true
                                isChosenBtn = 0
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_calendar_24),
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(date.format(DateTimeFormatter.ofPattern("dd.MM")))
                        }

                        BigButton(
                            modifier = Modifier
                                .weight(1f),
                            onClick = {
                                date = LocalDate.now()
                                updateSpots(startTime, endTime, date)
                                isChosenBtn = 1
                            },
                            isChosen = isChosenBtn == 1
                        ) {
                            Text(stringResource(R.string.today))
                        }

                        BigButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                date = LocalDate.now().plusDays(1)
                                updateSpots(startTime, endTime, date)
                                isChosenBtn = 2
                            },
                            isChosen = isChosenBtn == 2
                        ) {
                            Text(stringResource(R.string.tomorrow))
                        }
                    }
                }
            }
        }
    }

    bookingData?.let {
        BookObjectDetailScreen(
            onDismiss = { bookingData = null },
            bookingData = it,
            onBookClick = {
                onBookClick(it, { updateSpots(startTime, endTime, date) })
            },
            bookingStatus = bookingStatus
        )
    }

    if (showStartTimePicker) {
        MyTimePicker(
            onTimeSelected = {
                startTime = if (it.isBefore(LocalTime.now())) LocalTime.now().plusMinutes(10) else it
                endTime = startTime.plusMinutes(30)
                updateSpots(startTime, endTime, date)
                showStartTimePicker = false
            },
            onDismiss = {
                showStartTimePicker = false
            }
        )
    }

    if (showEndTimePicker) {
        MyTimePicker(
            onTimeSelected = {
                endTime = if (it.isBefore(startTime)) startTime.plusMinutes(30) else it
                updateSpots(startTime, endTime, date)
                showEndTimePicker = false
            },
            onDismiss = {
                showEndTimePicker = false
            }
        )
    }

    if (showDatePicker) {
        MyDatePicker(
            onDateSelected = {
                date = it
                updateSpots(startTime, endTime, date)
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}


@Composable
private fun BookingScreenPreview() {
    BookingScreenContent(
        isAdmin = true,
        coworking = Coworking(
            id = "",
            name = "т-ворк"
        ),
        bookingStatus = BookingStatus.Empty
    )
}

@Preview
@Composable
private fun BookingScreenDarkPreview() {
    MaterialTheme(
        colorScheme = DarkBlueTheme
    ) {
        BookingScreenPreview()
    }
}

@Preview
@Composable
private fun BookingScreenLightPreview() {
    MaterialTheme(
        colorScheme = LightBlueTheme
    ) {
        BookingScreenPreview()
    }
}