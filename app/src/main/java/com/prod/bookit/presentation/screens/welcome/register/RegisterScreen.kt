package com.prod.bookit.presentation.screens.welcome.register

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.prod.bookit.R
import com.prod.bookit.presentation.components.BigButton
import com.prod.bookit.presentation.components.InputField
import com.prod.bookit.presentation.models.Coworking
import com.prod.bookit.presentation.screens.RootNavDestinations
import com.prod.bookit.presentation.state.AuthState
import com.prod.bookit.presentation.viewModels.AuthViewModel
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import org.koin.androidx.compose.koinViewModel


@Composable
fun RegisterScreen(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val authState by authViewModel.authState.collectAsState()

    var avatarUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        uri: Uri? -> avatarUri = uri
    }

    val onImageClick: () -> Unit = {
        imagePickerLauncher.launch("image/*")
    }

    val onRegisterClick: (String, String, String, Boolean) -> Unit = { email, password, fullName, isAdmin ->
        authViewModel.register(
            email = email,
            password = password,
            fullName = fullName,
            isAdmin = isAdmin,
            avatarUri = avatarUri
        )
    }

    val yandexSdk = YandexAuthSdk.create(YandexAuthOptions(context))
    val yandexAuthLauncher = rememberLauncherForActivityResult(contract = yandexSdk.contract) { result ->
        handleYandexAuthResult(result)?.let { token ->
            authViewModel.signInWithYandex(token)
        }
    }

    val onSignInWithYandexClick: () -> Unit = {
        val loginOptions = YandexAuthLoginOptions()
        yandexAuthLauncher.launch(loginOptions)
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authorized -> {
                rootNavController.navigate(RootNavDestinations.Booking(coworkingId = Coworking.coworkings[0].id)) {
                    popUpTo(RootNavDestinations.Welcome) { inclusive = true }
                }
            }

            else -> Unit
        }
    }

    RegisterScreenContent(
        avatarUri = avatarUri,
        onImageClick = onImageClick,
        onRegisterClick = onRegisterClick,
        onSignInWithYandexClick = onSignInWithYandexClick,
        authState = authState
    )
}

@Composable
fun RegisterScreenContent(
    authState: AuthState,
    avatarUri: Uri?,
    onImageClick: () -> Unit,
    onSignInWithYandexClick: () -> Unit,
    onRegisterClick: (String, String, String, Boolean) -> Unit
) {
    val context = LocalContext.current

    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var nameError by rememberSaveable { mutableStateOf<String?>(null) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var isUserAdmin by rememberSaveable { mutableStateOf(false) }

    val isLoading = authState is AuthState.Loading

    val isRegisterButtonEnabled = name.isNotEmpty() &&
            email.isNotEmpty() &&
            password.isNotEmpty() &&
            emailError == null &&
            passwordError == null

    LaunchedEffect(authState) {
        if (authState is AuthState.Error) {
            emailError = context.getString(R.string.smth_went_wrong)
            passwordError = context.getString(R.string.smth_went_wrong)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.fill_in_application),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                AvatarPicker(avatarUri, onImageClick)

                Spacer(modifier = Modifier.height(16.dp))

                InputField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    label = stringResource(R.string.name),
                    leadingIcon = Icons.Default.Person,
                    onValueChange = {
                        name = it
                        nameError = if (it.isEmpty()) context.getString(R.string.field_cannot_be_empty) else null
                    },
                    error = nameError
                )

                Spacer(modifier = Modifier.height(8.dp))

                InputField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    label = stringResource(R.string.email),
                    leadingIcon = Icons.Default.Email,
                    onValueChange = {
                        email = it
                        emailError = when {
                            it.isEmpty() -> context.getString(R.string.field_cannot_be_empty)
                            !it.contains("@") -> context.getString(R.string.wrong_email_format)
                            else -> null
                        }
                    },
                    error = emailError
                )

                Spacer(modifier = Modifier.height(8.dp))

                InputField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    label = stringResource(R.string.password),
                    leadingIcon = Icons.Default.Lock,
                    onValueChange = {
                        password = it
                        passwordError = when {
                            it.isEmpty() -> context.getString(R.string.password_cannot_be_empty)
                            it.length < 4 -> context.getString(R.string.password_must_be_at_least_4_characters)
                            else -> null
                        }
                    },
                    error = passwordError,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = if (passwordVisible) R.drawable.visibility_24px else R.drawable.visibility_off_24px),
                                contentDescription = null
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                IsAdminSwitch(
                    state = isUserAdmin,
                    onCheckedChange = { isUserAdmin = !isUserAdmin }
                )

                Spacer(modifier = Modifier.height(8.dp))

                BigButton(
                    onClick = { onRegisterClick(email, password, name, isUserAdmin) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isRegisterButtonEnabled && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = stringResource(R.string.sign_up))
                    }
                }
            }
        }

        AuthentificationDivider()

        YandexSignInButton(onClick = onSignInWithYandexClick)
    }
}

@Composable
fun IsAdminSwitch(
    state: Boolean,
    onCheckedChange: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
        Switch(
            checked = state,
            onCheckedChange = { onCheckedChange() }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(R.string.enter_as_admin),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Preview
@Composable
private fun RegisterScreenPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface {
            RegisterScreenContent(
                AuthState.Unauthorized,
                null,
                {}, {}, { _, _, _, _ -> }
            )
        }
    }
}