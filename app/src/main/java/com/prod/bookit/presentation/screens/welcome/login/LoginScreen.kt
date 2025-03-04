package com.prod.bookit.presentation.screens.welcome.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.prod.bookit.presentation.viewModels.AuthViewModel
import com.prod.bookit.R
import com.prod.bookit.presentation.state.AuthState
import com.prod.bookit.presentation.components.BigButton
import com.prod.bookit.presentation.components.InputField
import com.prod.bookit.presentation.models.Coworking
import com.prod.bookit.presentation.screens.RootNavDestinations
import com.prod.bookit.presentation.screens.welcome.register.AuthentificationDivider
import com.prod.bookit.presentation.screens.welcome.register.YandexSignInButton
import com.prod.bookit.presentation.screens.welcome.register.handleYandexAuthResult
import com.prod.bookit.presentation.util.secondaryContentColor
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val authState by authViewModel.authState.collectAsState()

    when (authState) {
        is AuthState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is AuthState.Authorized -> {
            rootNavController.navigate(RootNavDestinations.Booking(coworkingId = Coworking.coworkings[0].id)) {
                popUpTo(RootNavDestinations.Welcome) { inclusive = true }
            }
        }

        is AuthState.Error -> {

        }

        else -> {

        }
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

    LoginScreenContent(
        authState = authState,
        onLoginClick = { email, password ->
            authViewModel.login(email, password)
        },
        onSignInWithYandexClick = onSignInWithYandexClick
    )
}

@Composable
private fun LoginScreenContent(
    authState: AuthState,
    onLoginClick: (email: String, password: String) -> Unit = { _, _ -> },
    onSignInWithYandexClick: () -> Unit = {}
) {
    val context = LocalContext.current

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }

    val isLoading = authState is AuthState.Loading

    LaunchedEffect(authState) {
        if (authState is AuthState.Error) {
            emailError = context.getString(R.string.invalid_data)
            passwordError = context.getString(R.string.invalid_data)
        }
    }

    val isButtonEnabled = email.isNotEmpty() &&
            password.isNotEmpty() &&
            emailError == null &&
            passwordError == null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        ElevatedCard(
            elevation = CardDefaults.elevatedCardElevation(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.bookit_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.welcome_back),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.enter_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = secondaryContentColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = when {
                            it.isEmpty() -> context.getString(R.string.email_cannot_be_empty)
                            !it.contains("@") -> context.getString(R.string.invalid_email_format)
                            else -> null
                        }
                    },
                    label = stringResource(R.string.email),
                    error = emailError,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                InputField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = when {
                            it.isEmpty() -> context.getString(R.string.password_cannot_be_empty)
                            it.length < 4 -> context.getString(R.string.password_must_be_at_least_4_characters)
                            else -> null
                        }
                    },
                    label = stringResource(R.string.password),
                    error = passwordError,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) R.drawable.visibility_24px else R.drawable.visibility_off_24px
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = ImageVector.vectorResource(image), contentDescription = null)
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                BigButton(
                    onClick = { onLoginClick(email, password) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isButtonEnabled && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(stringResource(R.string.sign_in))
                    }
                }
            }
        }

        AuthentificationDivider()

        YandexSignInButton(onClick = onSignInWithYandexClick)
    }
}


@Composable
private fun LoginScreenPreview() {
    Surface {
        LoginScreenContent(
            AuthState.Unauthorized
        )
    }
}

@Preview
@Composable
private fun LoginScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        LoginScreenPreview()
    }
}

@Preview
@Composable
private fun LoginScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        LoginScreenPreview()
    }
}