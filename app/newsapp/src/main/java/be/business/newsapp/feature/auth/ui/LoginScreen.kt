package be.business.newsapp.feature.auth.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import be.business.newsapp.core.presentation.collectEvents
import be.business.newsapp.feature.auth.presentation.AuthAction
import be.business.newsapp.feature.auth.presentation.AuthEvent
import be.business.newsapp.feature.auth.presentation.AuthState
import be.business.newsapp.feature.auth.presentation.AuthViewModel
import be.business.newsapp.ui.theme.AndroidPracticeTheme

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.collectEvents {
        when (it) {
            AuthEvent.LoginSuccess -> onLoginSuccess()
            is AuthEvent.ShowMessage -> {
                Toast.makeText(
                    context,
                    it.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    LoginContent(
        state = state,
        onEmailChange = { viewModel.action(AuthAction.EmailChanged(it)) },
        onPasswordChange = { viewModel.action(AuthAction.PasswordChanged(it)) },
        onSubmit = {
            viewModel.action(
                if (state.isRegisterMode) AuthAction.SubmitRegister else AuthAction.SubmitLogin
            )
        },
        onModeToggle = { viewModel.action(AuthAction.ModeChanged(!state.isRegisterMode)) }
    )
}

@Composable
private fun LoginContent(
    state: AuthState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onModeToggle: () -> Unit
) {
    val isRegisterMode = state.isRegisterMode
    val title = if (isRegisterMode) "Create Account" else "Login"
    val subtitle = if (isRegisterMode) {
        "Create your local account to save favorites"
    } else {
        "Sign in to access your favorites and profile"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.email,
                        onValueChange = onEmailChange,
                        label = { Text("Email") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.password,
                        onValueChange = onPasswordChange,
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (!isRegisterMode) {
                        Text(
                            text = "Forgot password?",
                            modifier = Modifier.align(Alignment.End),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    state.errorMessage?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Button(
                        onClick = onSubmit,
                        enabled = state.isFormValid && !state.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.height(18.dp)
                            )
                        } else {
                            Text(if (isRegisterMode) "Create account" else "Log in")
                        }
                    }

                    Text(
                        text = "Or continue with",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        SocialButton(
                            label = "Google",
                            modifier = Modifier.weight(1f)
                        )
                        SocialButton(
                            label = "Facebook",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isRegisterMode) "Already have an account? " else "Don't have account? ",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (isRegisterMode) "Login" else "Create now",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable(onClick = onModeToggle)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SocialButton(
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(44.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                shape = RoundedCornerShape(10.dp)
            ),
        shape = RoundedCornerShape(10.dp),
        color = Color.Transparent
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreviewLight() {
    AndroidPracticeTheme(darkTheme = false, dynamicColor = false) {
        LoginContent(
            state = AuthState(),
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {},
            onModeToggle = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreviewDark() {
    AndroidPracticeTheme(darkTheme = true, dynamicColor = false) {
        LoginContent(
            state = AuthState(isRegisterMode = true),
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {},
            onModeToggle = {}
        )
    }
}
