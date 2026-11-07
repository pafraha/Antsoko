package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AuthState
import com.example.ui.components.ContactAvatar
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardNavy
import com.example.ui.theme.CoralEndCall
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.MidnightNavy
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AuthScreen(
    authState: AuthState,
    onPhoneChange: (String) -> Unit,
    onOtpChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onCountryCodeChange: (String) -> Unit = {},
    onProceedToOtp: () -> Unit,
    onVerifyOtp: () -> Unit,
    onFillDemoOtp: () -> Unit,
    onCompleteRegistration: () -> Unit,
    onQuickDemoLogin: () -> Unit
) {
    if (authState.registrationStep == 1) {
        RegisterScreen(
            currentPhone = authState.inputPhone,
            currentCountryCode = authState.inputCountryCode,
            onPhoneChange = onPhoneChange,
            onCountryCodeChange = onCountryCodeChange,
            onContinue = onProceedToOtp,
            onQuickDemoLogin = onQuickDemoLogin
        )
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Brand Logo & Header
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(CyanAccent.copy(alpha = 0.15f))
                    .border(2.dp, CyanAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = CyanAccent,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ANTSOIRAY",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
            )

            Text(
                text = "Your Number. Your Communication. Your Control.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = CyanAccent,
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = CardNavy,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Text(
                    text = "Inscription avec votre numéro personnel (Sans numéro virtuel)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main Auth Flow Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardNavy),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (authState.registrationStep) {
                        1 -> {
                            // Step 1: Phone number
                            Text(
                                text = "Saisissez votre numéro",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Sélectionnez votre indicatif et entrez votre numéro SIM personnel.",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = authState.inputPhone,
                                onValueChange = onPhoneChange,
                                label = { Text("Numéro de téléphone") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = CyanAccent
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CyanAccent,
                                    unfocusedBorderColor = BorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_phone_input")
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onProceedToOtp,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyanAccent,
                                    contentColor = MidnightNavy
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_submit_phone_button")
                            ) {
                                Text("Recevoir le code OTP (Démo)", fontWeight = FontWeight.Bold)
                            }
                        }

                        2 -> {
                            // Step 2: OTP verification
                            Text(
                                text = "Vérification par code OTP",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Un code a été simulé vers ${authState.inputPhone}.",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = authState.inputOtp,
                                onValueChange = onOtpChange,
                                label = { Text("Code à 6 chiffres (123456)") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = CyanAccent
                                    )
                                },
                                singleLine = true,
                                isError = authState.otpError != null,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CyanAccent,
                                    unfocusedBorderColor = BorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_otp_input")
                            )

                            if (authState.otpError != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = authState.otpError,
                                    style = MaterialTheme.typography.bodySmall.copy(color = CoralEndCall)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Demo OTP Shortcut chip
                            Surface(
                                onClick = onFillDemoOtp,
                                shape = RoundedCornerShape(8.dp),
                                color = SurfaceElevated,
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.5f)),
                                modifier = Modifier.testTag("fill_demo_otp_chip")
                            ) {
                                Text(
                                    text = "⚡ Remplir le code démo fixe : 123456",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = CyanAccent,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onVerifyOtp,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EmeraldSuccess,
                                    contentColor = MidnightNavy
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_verify_otp_button")
                            ) {
                                Text("Valider le code", fontWeight = FontWeight.Bold)
                            }
                        }

                        3 -> {
                            // Step 3: Profile Completion
                            Text(
                                text = "Créez votre profil Antsoiray",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = authState.inputName,
                                onValueChange = onNameChange,
                                label = { Text("Nom et Prénom (ex: Franco Demo)") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = CyanAccent
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CyanAccent,
                                    unfocusedBorderColor = BorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_name_input")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = authState.inputEmail,
                                onValueChange = onEmailChange,
                                label = { Text("E-mail facultatif") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint = CyanAccent
                                    )
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CyanAccent,
                                    unfocusedBorderColor = BorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_email_input")
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onCompleteRegistration,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyanAccent,
                                    contentColor = MidnightNavy
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_complete_button")
                            ) {
                                Text("Accéder à l'application", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick 1-tap demo login button
            OutlinedButton(
                onClick = onQuickDemoLogin,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanAccent),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quick_demo_login_button")
            ) {
                Text("🚀 Connexion directe Démo : Franco Demo (+261 34 00 000 01)")
            }
        }
    }
}
