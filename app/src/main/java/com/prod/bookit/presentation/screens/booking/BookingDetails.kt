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
import kotlinx.coroutines.launch
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
    fullBookingInfo: FullBookingInfo
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Информация о бронировании",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Место №${fullBookingInfo.position}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        
        InfoCard(
            title = "Дата и время",
            content = listOf(
                "Дата: ${fullBookingInfo.date.format(dateFormatter)}",
                "С ${fullBookingInfo.timeFrom.format(timeFormatter)} до ${fullBookingInfo.timeUntil.format(timeFormatter)}"
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        InfoCard(
            title = "Информация о пользователе",
            photoUrl = fullBookingInfo.photoUrl,
            content = listOf(
                "Имя: ${fullBookingInfo.name}",
                "Email: ${fullBookingInfo.email}"
            ),
            icons = listOf(
                Icons.Default.Person,
                Icons.Default.Email
            )
        )
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
                    contentDescription = "Фото профиля",
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