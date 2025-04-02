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
import androidx.compose.material3.Text
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

    HandleAuthState(authState, rootNavController)
    
    val yandexSdk = YandexAuthSdk.create(YandexAuthOptions(context))
    val yandexAuthLauncher = rememberLauncherForActivityResult(contract = yandexSdk.contract) { result ->
        handleYandexAuthResult(result)?.let { token ->
            authViewModel.signInWithYandex(token)
        }
    }

    val onSignInWithYandexClick = {
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
private fun HandleAuthState(
    authState: AuthState,
    rootNavController: NavHostController
) {
    when (authState) {
        is AuthState.Loading -> {
            LoadingScreen()
        }
        is AuthState.Authorized -> {
            LaunchedEffect(Unit) {
                rootNavController.navigate(RootNavDestinations.Booking(coworkingId = Coworking.coworkings[0].id)) {
                    popUpTo(RootNavDestinations.Welcome) { inclusive = true }
                }
            }
        }
        else -> { }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
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
        LoginCard(
            email = email,
            onEmailChange = { 
                email = it
                emailError = validateEmail(it, context)
            },
            emailError = emailError,
            password = password,
            onPasswordChange = { 
                password = it
                passwordError = validatePassword(it, context)
            },
            passwordError = passwordError,
            passwordVisible = passwordVisible,
            onPasswordVisibilityChange = { passwordVisible = it },
            isButtonEnabled = isButtonEnabled,
            isLoading = isLoading,
            onLoginClick = { onLoginClick(email, password) }
        )
        
        AuthenticationDividerAndYandexButton(onSignInWithYandexClick)
    }
}

@Composable
private fun LoginCard(
    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String?,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordError: String?,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    isButtonEnabled: Boolean,
    isLoading: Boolean,
    onLoginClick: () -> Unit
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
                onValueChange = onEmailChange,
                label = stringResource(R.string.email),
                error = emailError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            InputField(
                value = password,
                onValueChange = onPasswordChange,
                label = stringResource(R.string.password),
                error = passwordError,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { onPasswordVisibilityChange(!passwordVisible) }) {
                        val visibilityIcon = if (passwordVisible) {
                            R.drawable.visibility_off_24px
                        } else {
                            R.drawable.visibility_24px
                        }
                        
                        Icon(
                            imageVector = ImageVector.vectorResource(visibilityIcon),
                            contentDescription = if (passwordVisible) {
                                stringResource(R.string.hide_password)
                            } else {
                                stringResource(R.string.show_password)
                            }
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            BigButton(
                onClick = onLoginClick,
                text = stringResource(R.string.sign_in),
                enabled = isButtonEnabled && !isLoading,
                isLoading = isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AuthenticationDividerAndYandexButton(
    onSignInWithYandexClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthentificationDivider()

        Spacer(modifier = Modifier.height(16.dp))

        YandexSignInButton(
            onClick = onSignInWithYandexClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun validateEmail(email: String, context: LocalContext): String? {
    return when {
        email.isEmpty() -> context.getString(R.string.email_cannot_be_empty)
        !email.contains("@") -> context.getString(R.string.invalid_email_format)
        else -> null
    }
}

private fun validatePassword(password: String, context: LocalContext): String? {
    return when {
        password.isEmpty() -> context.getString(R.string.password_cannot_be_empty)
        password.length < 6 -> context.getString(R.string.password_too_short)
        else -> null
    }
}