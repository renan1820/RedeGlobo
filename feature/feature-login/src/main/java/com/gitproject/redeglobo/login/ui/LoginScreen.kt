package com.gitproject.redeglobo.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gitproject.redeglobo.ui.theme.GloboBlack
import com.gitproject.redeglobo.ui.theme.GloboBlue
import com.gitproject.redeglobo.ui.theme.GloboBlueBright
import com.gitproject.redeglobo.ui.theme.GloboDarkCard
import com.gitproject.redeglobo.ui.theme.GloboDarkGray
import com.gitproject.redeglobo.ui.theme.GloboDarkSurface
import com.gitproject.redeglobo.ui.theme.GloboGray
import com.gitproject.redeglobo.ui.theme.GloboRed
import com.gitproject.redeglobo.ui.theme.GloboWhite
import androidx.compose.ui.tooling.preview.Preview
import com.gitproject.redeglobo.ui.theme.RedeGloboTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun mockLogin() {
        errorMessage = null
        isLoading = true
        scope.launch {
            delay(1200)
            isLoading = false
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GloboBlack)
    ) {
        // Subtle gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(GloboBlue.copy(alpha = 0.08f), GloboBlack),
                        endY = 600f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // Logo
            GloboLogo()

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "globeplay",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                ),
                color = GloboWhite
            )
            Text(
                text = "Assista onde quiser",
                style = MaterialTheme.typography.bodySmall,
                color = GloboGray
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                label = { Text("E-mail", color = GloboGray) },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = GloboGray)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = globoTextFieldColors(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = { Text("Senha", color = GloboGray) },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = GloboGray)
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = globoTextFieldColors(),
                shape = RoundedCornerShape(8.dp)
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage!!,
                    color = GloboRed,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            Button(
                onClick = { mockLogin() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GloboBlue,
                    contentColor = GloboWhite,
                    disabledContainerColor = GloboBlue.copy(alpha = 0.5f),
                    disabledContentColor = GloboWhite.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = GloboWhite,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Entrar",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = { /* mock forgot password */ }) {
                Text(
                    text = "Esqueci minha senha",
                    color = GloboGray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Divider
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = GloboDarkGray)
                Text(
                    text = "  ou continue com  ",
                    color = GloboGray,
                    style = MaterialTheme.typography.bodySmall
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = GloboDarkGray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Social login buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SocialLoginButton(
                    text = "Continuar com Google",
                    iconContent = { GoogleIcon() },
                    onClick = { mockLogin() }
                )
                SocialLoginButton(
                    text = "Continuar com Facebook",
                    iconContent = { FacebookIcon() },
                    onClick = { mockLogin() }
                )
                SocialLoginButton(
                    text = "Continuar com Apple",
                    iconContent = { AppleIcon() },
                    onClick = { mockLogin() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Register link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Não tem conta? ",
                    color = GloboGray,
                    style = MaterialTheme.typography.bodySmall
                )
                TextButton(
                    onClick = { mockLogin() },
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Cadastre-se",
                        color = GloboBlue,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SocialLoginButton(
    text: String,
    iconContent: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = GloboDarkCard,
            contentColor = GloboWhite
        ),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GloboDarkGray)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            iconContent()
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = GloboWhite
            )
        }
    }
}

@Composable
private fun GloboLogo() {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(GloboBlue),
        contentAlignment = Alignment.Center
    ) {
        // Stylized "G" using nested boxes/shapes to approximate Globo logo
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .border(5.dp, GloboWhite, CircleShape)
        )
        // Horizontal bar of G (right side cutout)
        Box(
            modifier = Modifier
                .width(16.dp)
                .height(5.dp)
                .align(Alignment.CenterEnd)
                .padding(end = 0.dp)
                .background(GloboBlue)
        )
        Box(
            modifier = Modifier
                .width(16.dp)
                .height(5.dp)
                .align(Alignment.CenterEnd)
                .background(GloboWhite)
        )
    }
}

@Composable
private fun GoogleIcon() {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(GloboWhite),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "G",
            color = Color(0xFF4285F4),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun FacebookIcon() {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(Color(0xFF1877F2)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "f",
            color = GloboWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AppleIcon() {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(GloboWhite),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "",
            color = GloboBlack,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
    }
}

// ─── Previews ────────────────────────────────────────────────────────────────

@Preview(name = "Login Screen", showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun LoginScreenPreview() {
    RedeGloboTheme {
        LoginScreen(onLoginSuccess = {})
    }
}

// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun globoTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor      = GloboBlue,
    unfocusedBorderColor    = GloboDarkGray,
    focusedTextColor        = GloboWhite,
    unfocusedTextColor      = GloboWhite,
    cursorColor             = GloboBlue,
    focusedLabelColor       = GloboBlue,
    unfocusedLabelColor     = GloboGray,
    focusedContainerColor   = GloboDarkSurface,
    unfocusedContainerColor = GloboDarkSurface,
    focusedLeadingIconColor = GloboBlue,
)
