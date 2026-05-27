package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.ReportGmailerrorred
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.HotPinkGlow
import com.example.ui.theme.SapphireDark
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.CoolGray
import com.example.ui.theme.GlassBorder
import com.example.ui.viewmodel.MarketplaceViewModel

@Composable
fun AdminPanelScreen(
    viewModel: MarketplaceViewModel,
    isDarkTheme: Boolean = true
) {
    val context = LocalContext.current
    val allProducts by viewModel.adminAllProducts.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.activeScreen.value = "profile" }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "ADMIN COGNITIVE HUB",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                    Text(
                        text = "MARKET LAWS MONITOR & SYSTEM MODERATION",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = HotPinkGlow,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    ) { paddingValues ->
        CyberBackdrop(modifier = Modifier.fillMaxSize(), isDarkTheme = isDarkTheme) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Dashboard analytics grid row
                item {
                    Text(
                        text = "Real-Time System Metrics",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoolGray
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Metric 1: users count
                        GlassCard(
                            modifier = Modifier.weight(1f),
                            isDarkTheme = isDarkTheme
                        ) {
                            Column(modifier = Modifier.padding(2.dp)) {
                                Icon(Icons.Default.PeopleAlt, contentDescription = "u", tint = CyberBlue, modifier = Modifier.size(18.dp))
                                Text("3.2k Node Members", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                                Text("Online Active Users", fontSize = 8.sp, color = CoolGray)
                            }
                        }

                        // Metric 2: product listings count
                        GlassCard(
                            modifier = Modifier.weight(1f),
                            isDarkTheme = isDarkTheme
                        ) {
                            Column(modifier = Modifier.padding(2.dp)) {
                                Icon(Icons.Default.Leaderboard, contentDescription = "u", tint = SuccessGreen, modifier = Modifier.size(18.dp))
                                Text("${allProducts.size} Active Ads", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
                                Text("Live Listings Index", fontSize = 8.sp, color = CoolGray)
                            }
                        }
                    }
                }

                // Listing Moderation Management Queue
                item {
                    Text(
                        text = "Listings Inspection Center & soft delete logs",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoolGray
                    )
                }

                if (allProducts.isEmpty()) {
                    item {
                        Text("No listings verified inside Room database repositories.", fontSize = 11.sp, color = CoolGray, modifier = Modifier.padding(start = 8.dp))
                    }
                } else {
                    items(allProducts) { p ->
                        GlassCard(
                            isDarkTheme = isDarkTheme,
                            borderColor = if (p.isApproved) GlassBorder else HotPinkGlow.copy(alpha = 0.4f)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                    Text(p.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Seller: ${p.sellerName} • Category: ${p.category} • price: Rs. ${p.price}", fontSize = 10.sp, color = CoolGray)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CyberPulseGlow(
                                            size = 4.dp,
                                            glowColor = if (p.isApproved) SuccessGreen else HotPinkGlow
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (p.isApproved) "AD Live on Feed" else "Awaiting Admin Verification Check",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (p.isApproved) SuccessGreen else HotPinkGlow
                                        )
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    // toggle approval status
                                    IconButton(
                                        onClick = {
                                            viewModel.toggleProductApproval(p.id)
                                            Toast.makeText(context, "Authorization updated successfully", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.background(SapphireDark, CircleShape).size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (p.isApproved) Icons.Default.Block else Icons.Default.CheckCircle,
                                            contentDescription = "toggle auth status",
                                            tint = if (p.isApproved) HotPinkGlow else SuccessGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    // delete permanently
                                    IconButton(
                                        onClick = {
                                            viewModel.removeProductListing(p.id)
                                            Toast.makeText(context, "Listing purged permanently", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.background(SapphireDark, CircleShape).size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Purge node ad",
                                            tint = HotPinkGlow,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Fraud Reports section logs
                item {
                    Text(
                        text = "System Fraud Audit reports indices",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoolGray
                    )
                }

                item {
                    GlassCard(borderColor = HotPinkGlow.copy(alpha = 0.5f), isDarkTheme = isDarkTheme) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ReportGmailerrorred, contentDescription = "r", tint = HotPinkGlow, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("REPORT ID-8409: Duplicate Advertising spam", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                                Text("Sanjeewa D. flagged: User Kamal K. duplicated listings in Property category.", fontSize = 9.sp, color = CoolGray)
                            }
                        }
                    }
                }
            }
        }
    }
}
