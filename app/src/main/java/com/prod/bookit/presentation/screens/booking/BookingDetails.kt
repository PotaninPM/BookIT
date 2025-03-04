package com.prod.bookit.presentation.screens.booking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.prod.bookit.R
import com.prod.bookit.presentation.models.FullBookingInfo
import com.prod.bookit.presentation.theme.LightBlueTheme
import com.prod.bookit.presentation.viewModels.BookingViewModel
import com.prod.bookit.presentation.viewModels.ProfileViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetails(
    spotId: String,
    vm: BookingViewModel,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    
    var bookingInfo by remember { mutableStateOf<FullBookingInfo?>(null) }
    
    LaunchedEffect(spotId) {
        try {
            bookingInfo = vm.getCurrentBookingForSpot(spotId)
        } catch (e: Exception) {
            e.printStackTrace()
            coroutineScope.launch {
                sheetState.hide()
                onDismiss()
            }
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        bookingInfo?.let { info ->
            BookingDetailsContent(
                fullBookingInfo = info
            )
        } ?: LoadingScreen()
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun BookingDetailsContent(
    fullBookingInfo: FullBookingInfo,
    vm: ProfileViewModel = koinViewModel()
) {
    var showDialog by remember { mutableStateOf<FullBookingInfo?>(null) }

    if (showDialog != null) {
        ChangeUserInfoDialog(
            fullBookingInfo.id,
            fullBookingInfo.email,
            fullBookingInfo.name,
            onDismiss = {
                showDialog = null
            },
            onConfirm = { email, name, userId ->
                vm.changeUserInfo(userId, email, name)
                showDialog = null
            }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.bronirovanie_info),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(R.string.place_number, fullBookingInfo.position),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        
        InfoCard(
            title = stringResource(R.string.date_and_time),
            content = listOf(
                stringResource(R.string.date, fullBookingInfo.date.format(dateFormatter)),
                stringResource(
                    R.string.from_to,
                    fullBookingInfo.timeFrom.format(timeFormatter),
                    fullBookingInfo.timeUntil.format(timeFormatter)
                )
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        InfoCard(
            title = stringResource(R.string.user_info),
            photoUrl = fullBookingInfo.photoUrl,
            content = listOf(
                stringResource(R.string.namee, fullBookingInfo.name),
                stringResource(R.string.emaill, fullBookingInfo.email)
            ),
            icons = listOf(
                Icons.Default.Person,
                Icons.Default.Email
            )
        )

        Button(
            onClick = {
                showDialog = fullBookingInfo
            }
        ) {
            Text("Изменить данные юзера")
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    content: List<String>,
    photoUrl: String? = null,
    icons: List<ImageVector>? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (photoUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(photoUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            content.forEachIndexed { index, text ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    if (icons != null && index < icons.size) {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun BookingDetailsPreview() {
    MaterialTheme(
        colorScheme = LightBlueTheme
    ) {
        Surface {
            BookingDetailsContent(
                fullBookingInfo = FullBookingInfo(
                    position = 12,
                    date = LocalDate.now(),
                    timeFrom = LocalTime.of(14, 0),
                    timeUntil = LocalTime.of(16, 0),
                    photoUrl = "sdafgh",
                    name = "Иван Иванов",
                    email = "ivan@example.com",
                    status = "active"
                )
            )
        }
    }

}