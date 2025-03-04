package com.prod.bookit.presentation.screens.coworkings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.prod.bookit.R
import com.prod.bookit.domain.model.CoworkingDetail
import com.prod.bookit.domain.model.CoworkingSummary
import com.prod.bookit.presentation.components.FullScreenImageDialog
import com.prod.bookit.presentation.screens.RootNavDestinations
import com.prod.bookit.presentation.viewModels.CoworkingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CoworkingsScreen(
    rootNavController: NavController,
    viewModel: CoworkingsViewModel = koinViewModel(),
    onChooseCoworkingClick: (CoworkingDetail) -> Unit = {}
) {
    val coworkings by viewModel.coworkings.collectAsState()
    val selectedDetail by viewModel.selectedDetail.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadCoworkings()
    }

    LaunchedEffect(selectedDetail) {
        showBottomSheet = selectedDetail != null
    }

    if (selectedImage.isNotEmpty()) {
        FullScreenImageDialog(
            onDismiss = {
                selectedImage = ""
            },
            imageUrl = selectedImage
        )
    }

    if (showBottomSheet && selectedDetail != null) {
        CoworkingBottomSheet(
            detail = selectedDetail!!,
            onDismiss = {
                showBottomSheet = false
                viewModel.clearSelectedDetail()
            },
            onImageClick = { image ->
                selectedImage = image
            },
            onChooseCoworkingClick = {
                onChooseCoworkingClick(it)
                showBottomSheet = false
                rootNavController.navigate(RootNavDestinations.Booking(it.id))
                viewModel.clearSelectedDetail()
            }
        )
    }

    CoworkingsScreenContent(
        coworkings = coworkings,
        onCoworkingClick = { summary ->
            viewModel.loadCoworkingDetail(summary.id)
        },
        onBackClicked = {
            rootNavController.navigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CoworkingsScreenContent(
    coworkings: List<CoworkingSummary> = emptyList(),
    onCoworkingClick: (CoworkingSummary) -> Unit = {},
    onBackClicked: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.coworkings)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackClicked()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                coworkings, key = { it.id }
            ) { coworking ->
                CoworkingItem(
                    coworking = coworking,
                    onCoworkingClick = {
                        onCoworkingClick(coworking)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun AllCoworkingsScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        //CoworkingsScreenContent()
    }
}

@Preview
@Composable
private fun AllCoworkingsScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        //CoworkingsScreenContent()
    }
}

