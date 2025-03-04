package com.prod.bookit.presentation.screens.coworkings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.prod.bookit.R
import com.prod.bookit.domain.model.CoworkingDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoworkingBottomSheet(
    detail: CoworkingDetail,
    onDismiss: () -> Unit,
    onImageClick: (String) -> Unit,
    onChooseCoworkingClick: (CoworkingDetail) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (detail.images.isNotEmpty()) {
                val imageUrls = detail.images

                when (imageUrls.size) {
                    1 -> Image(
                        painter = rememberAsyncImagePainter(imageUrls.first()),
                        contentDescription = null,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    else -> {
                        LazyRow(
                            modifier = Modifier
                                .pointerInput(Unit) {
                                    detectVerticalDragGestures { change, _ ->
                                        change.consume()
                                    }
                                }
                        ) {
                            items(imageUrls) { image ->
                                Image(
                                    painter = rememberAsyncImagePainter(image),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(200.dp)
                                        .width(300.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            onImageClick(image)
                                        },
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(R.string.no_photo), color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = stringResource(R.string.reshim_work, detail.opensAt.substring(0..4), detail.endsAt.substring(0..4)),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = detail.address,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = detail.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onChooseCoworkingClick(detail)
                          },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.choose))
            }
        }
    }
}

@Preview
@Composable
private fun CoworkingBottomSheetScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        //CoworkingBottomSheet()
    }
}

@Preview
@Composable
private fun CoworkingBottomSheetScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        //CoworkingBottomSheet()
    }
}

