package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.OnDeviceTraining
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.HotPinkGlow
import com.example.ui.viewmodel.MarketplaceViewModel

@Composable
fun AuthScreen(
    viewModel: MarketplaceViewModel,
    isDarkTheme: Boolean = true
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Email Login, 1: Phone OTP, 2: Register
    var email by remember { mutableStateOf("SanjeewaD87@gmail.com") }
    var password by remember { mutableStateOf("premium1234") }
    var phoneNumber by remember { mutableStateOf("+94 77 123 4567") }
    var otpCode by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }

    var registerName by remember { mutableStateOf("") }
    var registerEmail by remember { mutableStateOf("") }
    var registerPw by remember { mutableStateOf("") }

    var showForgotDialog by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    CyberBackdrop(modifier = Modifier.fillMaxSize(), isDarkTheme = isDarkTheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Premium Animated Logo Icon
            CyberPulseGlow(size = 20.dp, glowColor = CyberBlue)

            Spacer(modifier = Modifier.height(16.dp))

            // AGK Marketplace Title
            Text(
                text = "AGK MARKETPLACE",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                letterSpacing = 2.sp,
                color = if (isDarkTheme) CyberBlue else ElectricBlue,
                textAlign = TextAlign.Center
            )

            Text(
                text = "AI-POWERED FUTURISTIC DIRECT PORTAL",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = if (isDarkTheme) Color.White.copy(alpha = 0.5f) else Color.DarkGray.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Auth Tabs Card Container
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                isDarkTheme = isDarkTheme
            ) {
                Column {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        contentColor = if (isDarkTheme) Color.White else Color.Black,
                        divider = {},
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = if (isDarkTheme) CyberBlue else ElectricBlue
                            )
                        }
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Email", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Phone OTP", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = { Text("Sign Up", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Inner forms based on target Auth Mode
                    when (selectedTab) {
                        0 -> { // EMAIL LOGIN
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email Address") },
                                leadingIcon = { Icon(Icons.Default.Mail, contentDescription = "Email", tint = CyberBlue) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("username_input"),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                colors = getTextFieldColors(isDarkTheme)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Access Code Password") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Locks", tint = CyberBlue) },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = getTextFieldColors(isDarkTheme)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Forgot Password Text Action
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = "Recovery Code?",
                                    color = CyberBlue,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable { showForgotDialog = true }
                                        .padding(8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { viewModel.login(email) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("login_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ElectricBlue,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("DECRYPT & INITIALIZE SECURE LOGIN", fontWeight = FontWeight.Bold)
                            }
                        }

                        1 -> { // PHONE OTP LOGIN
                            Column(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = phoneNumber,
                                    onValueChange = { phoneNumber = it },
                                    label = { Text("Phone Identifier") },
                                    placeholder = { Text("+94 77 XXX XXXX") },
                                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone", tint = CyberBlue) },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    colors = getTextFieldColors(isDarkTheme)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                AnimatedVisibility(visible = otpSent) {
                                    Column {
                                        OutlinedTextField(
                                            value = otpCode,
                                            onValueChange = { otpCode = it },
                                            label = { Text("6-Digit OTP Cipher") },
                                            leadingIcon = { Icon(Icons.Default.Security, contentDescription = "Security", tint = HotPinkGlow) },
                                            modifier = Modifier.fillMaxWidth(),
                                            singleLine = true,
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            colors = getTextFieldColors(isDarkTheme)
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }

                                if (!otpSent) {
                                    Button(
                                        onClick = { otpSent = true },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                                    ) {
                                        Text("REQUEST SIGN-IN CIPHER PIN", fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Button(
                                        onClick = { viewModel.login("otp_$phoneNumber@agk.com") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = CyberBlue, contentColor = Color.Black)
                                    ) {
                                        Text("VERIFY & BOOTSTRAP GATEWAY", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        2 -> { // SIGN-UP REGISTER FORM
                            OutlinedTextField(
                                value = registerName,
                                onValueChange = { registerName = it },
                                label = { Text("Full Legal Name") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "User", tint = CyberBlue) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = getTextFieldColors(isDarkTheme)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = registerEmail,
                                onValueChange = { registerEmail = it },
                                label = { Text("Email Sync") },
                                leadingIcon = { Icon(Icons.Default.Mail, contentDescription = "Mail", tint = CyberBlue) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = getTextFieldColors(isDarkTheme)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = registerPw,
                                onValueChange = { registerPw = it },
                                label = { Text("Secure Access Code Pass") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock", tint = CyberBlue) },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = getTextFieldColors(isDarkTheme)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    if (registerEmail.isNotEmpty()) {
                                        viewModel.login(registerEmail)
                                    } else {
                                        viewModel.login("new_user_marketplace@agk.com")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                            ) {
                                Text("PROVISION UNIQUE AGK IDENTITY ID", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Google sign-in layout button separator
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "— OR AUTHORIZE VAULT WITH ONE-TAP INTERMEDARY —",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color.White.copy(alpha = 0.4f) else Color.DarkGray.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Premium Google Sign-In Styled Card button with brand guidelines
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable { viewModel.login("SanjeewaD87@gmail.com") },
                cornerRadius = 24.dp,
                isDarkTheme = isDarkTheme
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.OnDeviceTraining, // Representative neural glow
                        contentDescription = "Oauth Link",
                        tint = CyberBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Sign-In with G-Suite Vault Token",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
        }

        // Recovery dialog
        if (showForgotDialog) {
            AlertDialog(
                onDismissRequest = { showForgotDialog = false },
                title = { Text("Initiate Identity Recovery Cipher", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("Specify registered email context. A recovery key code will be sent to decrypt your access configurations.")
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = forgotEmail,
                            onValueChange = { forgotEmail = it },
                            label = { Text("Mail Address Link") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = getTextFieldColors(isDarkTheme)
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showForgotDialog = false }) {
                        Text("DISPATCH CIPHER RECOVERY KEY")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showForgotDialog = false }) {
                        Text("ABORT", color = HotPinkGlow)
                    }
                }
            )
        }
    }
}
