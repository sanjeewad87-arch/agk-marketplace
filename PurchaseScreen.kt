package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationEntity
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.CoolGray
import com.example.ui.theme.HotPinkGlow
import com.example.ui.theme.SapphireDark
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.MarketplaceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PurchaseScreen(
    viewModel: MarketplaceViewModel,
    isDarkTheme: Boolean = true
) {
    val context = LocalContext.current
    val productId by viewModel.activeProductDetailId.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()

    val pItem = allProducts.find { it.id == productId }

    // Stepper checks
    var paymentStep by remember { mutableStateOf(0) } // 0: Select Method, 1: Processing, 2: Receipt Complete
    var paymentMethod by remember { mutableStateOf("stripe") } // stripe, payhere, cod

    // Credit Card inputs
    var ccNumber by remember { mutableStateOf("4242 •••• •••• 4242") }
    var ccExp by remember { mutableStateOf("12 / 29") }
    var ccCvc by remember { mutableStateOf("101") }

    // PayHere inputs
    var lkrRatePhone by remember { mutableStateOf("+94 77 123 4567") }
    var lkrPinCode by remember { mutableStateOf("") }

    if (pItem == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Product parameters not loaded...", color = HotPinkGlow)
        }
        return
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.activeScreen.value = "details" }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = viewModel.translate("checkout_headline"),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                    Text(
                        text = "ENCRYPTED TRANSACTIONS GATEWAY",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberBlue,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    ) { paddingValues ->
        CyberBackdrop(modifier = Modifier.fillMaxSize(), isDarkTheme = isDarkTheme) {
            when (paymentStep) {
                0 -> { // SELECT METHOD & DETAILS
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Summary Card
                        item {
                            GlassCard(isDarkTheme = isDarkTheme) {
                                Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                                    Text("Order Summary", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CoolGray)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(pItem.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("Merchant: ${pItem.sellerName} • location: ${pItem.location}", fontSize = 11.sp, color = CoolGray)
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Total Bill Details", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = "Rs. ${pItem.price}",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = CyberBlue
                                        )
                                    }
                                }
                            }
                        }

                        // Gateway selectors
                        item {
                            Text("Available Gateways sync", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CoolGray)
                        }

                        // stripe selector card
                        item {
                            GlassCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { paymentMethod = "stripe" },
                                borderColor = if (paymentMethod == "stripe") CyberBlue else GlassBorder,
                                isDarkTheme = isDarkTheme
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CyberPulseGlow(size = 4.dp, glowColor = if (paymentMethod == "stripe") CyberBlue else Color.Transparent)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Icon(Icons.Default.CreditCard, contentDescription = "c", tint = CyberBlue, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("Stripe secure Wallet", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("International Credit / Debit portals card (USD)", fontSize = 9.sp, color = CoolGray)
                                    }
                                }
                            }
                        }

                        // Stripe Form details input (Visible only when stripe selected)
                        item {
                            AnimatedVisibility(visible = paymentMethod == "stripe") {
                                GlassCard(isDarkTheme = isDarkTheme, borderColor = CyberBlue.copy(alpha = 0.25f)) {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        OutlinedTextField(
                                            value = ccNumber,
                                            onValueChange = { ccNumber = it },
                                            label = { Text("Mock card Number") },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = getTextFieldColors(isDarkTheme)
                                        )
                                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                            OutlinedTextField(
                                                value = ccExp,
                                                onValueChange = { ccExp = it },
                                                label = { Text("Expiration") },
                                                modifier = Modifier.weight(1f),
                                                colors = getTextFieldColors(isDarkTheme)
                                            )
                                            OutlinedTextField(
                                                value = ccCvc,
                                                onValueChange = { ccCvc = it },
                                                label = { Text("CVC") },
                                                modifier = Modifier.weight(1f),
                                                colors = getTextFieldColors(isDarkTheme)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // PayHere Sri Lanka selector card
                        item {
                            GlassCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { paymentMethod = "payhere" },
                                borderColor = if (paymentMethod == "payhere") CyberBlue else GlassBorder,
                                isDarkTheme = isDarkTheme
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CyberPulseGlow(size = 4.dp, glowColor = if (paymentMethod == "payhere") CyberBlue else Color.Transparent)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Icon(Icons.Default.Payments, contentDescription = "p", tint = SuccessGreen, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("PayHere Sri Lanka Gateway", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("LKR Currency Conversion local gateways", fontSize = 9.sp, color = CoolGray)
                                    }
                                }
                            }
                        }

                        item {
                            AnimatedVisibility(visible = paymentMethod == "payhere") {
                                GlassCard(isDarkTheme = isDarkTheme, borderColor = SuccessGreen.copy(alpha = 0.25f)) {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        val lkrEquiv = pItem.price * 300.0 // Mock LKR rate conversion
                                        Text(
                                            text = "Converted Payment charge: LKR $lkrEquiv LKR (Flat 300 LKR Exchange index)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SuccessGreen
                                        )
                                        OutlinedTextField(
                                            value = lkrRatePhone,
                                            onValueChange = { lkrRatePhone = it },
                                            label = { Text("Local phone (for otp confirmation)") },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = getTextFieldColors(isDarkTheme)
                                        )
                                    }
                                }
                            }
                        }

                        // Cash on Delivery selector card
                        item {
                            GlassCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { paymentMethod = "cod" },
                                borderColor = if (paymentMethod == "cod") CyberBlue else GlassBorder,
                                isDarkTheme = isDarkTheme
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CyberPulseGlow(size = 4.dp, glowColor = if (paymentMethod == "cod") CyberBlue else Color.Transparent)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Icon(Icons.Default.LocalShipping, contentDescription = "s", tint = HotPinkGlow, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("Cash on Delivery (COD)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("Inspect parcels physically upon delivery", fontSize = 9.sp, color = CoolGray)
                                    }
                                }
                            }
                        }

                        // COD Details
                        item {
                            AnimatedVisibility(visible = paymentMethod == "cod") {
                                GlassCard(isDarkTheme = isDarkTheme) {
                                    Text(
                                        text = "By choosing COD, your shipping address matches your registration parameters. Courier charges are paid upon receipt. Product testing is authorized beforehand.",
                                        fontSize = 10.sp,
                                        color = CoolGray
                                    )
                                }
                            }
                        }

                        // Authenticate payment CTA button
                        item {
                            Button(
                                onClick = {
                                    paymentStep = 1 // Shift to processing
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("submit_payment_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LockOpen, contentDescription = "payment authorization lock", tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("CONFIRM DEPOSIT PAYMENT & PAY NOW", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                1 -> { // DEPOSIT PROCESSING SCREEN
                    LaunchedEffect(Unit) {
                        delay(2400) // Simulate ledger authorization lock delay times

                        // Notify system profile of balance change and add transactions
                        val currentProfileDirect = viewModel.repository.getProfileDirect()
                        if (paymentMethod == "stripe") {
                            viewModel.repository.saveProfile(
                                currentProfileDirect.copy(
                                    balanceStripeUSD = currentProfileDirect.balanceStripeUSD + pItem.price
                                )
                            )
                        } else if (paymentMethod == "payhere") {
                            viewModel.repository.saveProfile(
                                currentProfileDirect.copy(
                                    balancePayHereLKR = currentProfileDirect.balancePayHereLKR + (pItem.price * 300.0)
                                )
                            )
                        }

                        viewModel.repository.insertNotification(
                            NotificationEntity(
                                title = "Transaction Staged - SUCCESS",
                                content = "Paid Rs. ${pItem.price} securely for '${pItem.title}' by ${paymentMethod.uppercase()} wallet codes.",
                                type = "ORDER"
                            )
                        )

                        paymentStep = 2 // Transition to success receipt
                    }

                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(54.dp), color = CyberBlue, strokeWidth = 5.dp)
                        Spacer(modifier = Modifier.height(18.dp))
                        Text("Staging Secure ledger channels...", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Encoding cryptographic signature. Conforming payment hashes with Stripe & PayHere Sri Lanka index boards.",
                            color = CoolGray,
                            fontSize = 11.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                2 -> { // TRANSACTION COMPLETE RECEIPT
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Success glow node
                        CyberPulseGlow(glowColor = SuccessGreen, size = 30.dp)
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("PAYMENT COMPLETED SECURELY!", fontWeight = FontWeight.Black, fontSize = 20.sp, color = SuccessGreen)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Invoice Receipt has been synchronized to identity history profile ledgers.",
                            fontSize = 12.sp,
                            color = CoolGray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        GlassCard(isDarkTheme = isDarkTheme) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Receipt TX-${System.currentTimeMillis().toString().takeLast(8)}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Product", fontSize = 11.sp, color = CoolGray)
                                    Text(pItem.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.width(140.dp))
                                }
                                Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Merchant Node", fontSize = 11.sp, color = CoolGray)
                                    Text(pItem.sellerName, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Payment channel", fontSize = 11.sp, color = CoolGray)
                                    Text(paymentMethod.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberBlue)
                                }
                                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = GlassBorder)
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Transacted charge", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("Rs. ${pItem.price}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = SuccessGreen)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        Button(
                            onClick = {
                                viewModel.activeScreen.value = "home"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Cached, contentDescription = "r", tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("SYNCHRONIZE & RETURN TO HOME", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
