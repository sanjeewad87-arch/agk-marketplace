package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfileEntity
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.CoolGray
import com.example.ui.theme.HotPinkGlow
import com.example.ui.theme.SapphireDark
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.MarketplaceViewModel

@Composable
fun ProfileScreen(
    viewModel: MarketplaceViewModel,
    isDarkTheme: Boolean = true,
    onThemeToggle: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val currentProfile by viewModel.userProfile.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val allProducts by viewModel.adminAllProducts.collectAsState()
    val currentLang by viewModel.activeLanguage.collectAsState()
    val savedSearches by viewModel.savedSearches.collectAsState()
    val recentlyViewed by viewModel.recentlyViewed.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()

    var bioText by remember { mutableStateOf("") }
    var userProfileInitialized by remember { mutableStateOf<UserProfileEntity?>(null) }

    // Dropdown toggles
    var listingsExpanded by remember { mutableStateOf(false) }
    var notifExpanded by remember { mutableStateOf(false) }
    var purchasesExpanded by remember { mutableStateOf(false) }
    var savedSearchesExpanded by remember { mutableStateOf(false) }
    var recentlyExpanded by remember { mutableStateOf(false) }

    if (currentProfile != null && userProfileInitialized == null) {
        userProfileInitialized = currentProfile
        bioText = currentProfile!!.bio
    }

    val activeProfile = currentProfile ?: UserProfileEntity()
    val myProducts = allProducts.filter { it.sellerName == activeProfile.name }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "IDENTITY CENTER",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                    Text(
                        text = "AUTHENTICATION PROTOCOL SYNCED",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberBlue,
                        letterSpacing = 0.5.sp
                    )
                }

                // ADMIN PANEL CTA BAR
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SapphireDark)
                        .border(1.dp, CyberBlue, shape = RoundedCornerShape(12.dp))
                        .clickable { viewModel.activeScreen.value = "admin" }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = "adm", tint = CyberBlue, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Admin Panel",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberBlue
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
                // Identity Hub user info card
                item {
                    GlassCard(isDarkTheme = isDarkTheme) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Dynamic premium avatar index frame
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(ElectricBlue, CircleShape)
                                        .border(2.dp, CyberBlue, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = "p", tint = Color.White, modifier = Modifier.size(36.dp))
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        text = activeProfile.name,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 18.sp,
                                        color = if (isDarkTheme) Color.White else Color.Black
                                    )
                                    Text(
                                        text = activeProfile.email,
                                        fontSize = 11.sp,
                                        color = CoolGray
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = "r", tint = HotPinkGlow, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text("${activeProfile.rating} score", fontSize = 10.sp, color = HotPinkGlow, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Joined ${activeProfile.joinedDate}", fontSize = 9.sp, color = CoolGray)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Bio editing
                            OutlinedTextField(
                                value = bioText,
                                onValueChange = {
                                    bioText = it
                                    viewModel.saveProfile(activeProfile.copy(bio = it))
                                },
                                label = { Text("Describe yourself / business bio index") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = getTextFieldColors(isDarkTheme)
                            )
                        }
                    }
                }

                // Balance summary
                item {
                    GlassCard(isDarkTheme = isDarkTheme, borderColor = CyberBlue.copy(alpha = 0.4f)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("PayHere LKR balance", fontSize = 9.sp, color = CoolGray, fontWeight = FontWeight.Bold)
                                Text("LKR ${activeProfile.balancePayHereLKR}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = SuccessGreen)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Stripe Wallet Balance", fontSize = 9.sp, color = CoolGray, fontWeight = FontWeight.Bold)
                                Text("$${activeProfile.balanceStripeUSD}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = CyberBlue)
                            }
                        }
                    }
                }

                // Localization selections ( Sinhala, Tamil, English )
                item {
                    Text(
                        text = "Visual System Localization Preferences",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoolGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    GlassCard(isDarkTheme = isDarkTheme) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Language, contentDescription = "l", tint = CyberBlue, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Switch Region language", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf("EN", "SI", "TA").forEach { ln ->
                                        val isSelected = currentLang == ln
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSelected) ElectricBlue else SapphireDark)
                                                .border(1.dp, if (isSelected) CyberBlue else GlassBorder, shape = RoundedCornerShape(8.dp))
                                                .clickable { viewModel.activeLanguage.value = ln }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = if (ln == "EN") "English" else if (ln == "SI") "Sinhala" else "Tamil",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else CyberBlue
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Theme Selection
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                                        contentDescription = "theme icon",
                                        tint = CyberBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Dark Theme Mode", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    listOf(true, false).forEach { d ->
                                        val isSelected = isDarkTheme == d
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSelected) ElectricBlue else SapphireDark)
                                                .border(1.dp, if (isSelected) CyberBlue else GlassBorder, shape = RoundedCornerShape(8.dp))
                                                .clickable { onThemeToggle(d) }
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = if (d) "Dark" else "Light",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else CyberBlue
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Summary Stat Badge Header Area
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = if (currentLang == "SI") "ගැනුම්කරු සහ විකුණුම්කරු උපකරණ පුවරුව" else "BUYER & SELLER AGK DASHBOARD",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = CyberBlue,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        GlassCard(isDarkTheme = isDarkTheme, borderColor = CyberBlue.copy(alpha = 0.3f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("SALES VOLUME", fontSize = 8.sp, color = CoolGray, fontWeight = FontWeight.Bold)
                                    Text("$43,750 USD", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                                }
                                Box(modifier = Modifier.width(1.dp).height(24.dp).background(GlassBorder))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("TOTAL ADS", fontSize = 8.sp, color = CoolGray, fontWeight = FontWeight.Bold)
                                    Text("${myProducts.size} Items", fontSize = 14.sp, fontWeight = FontWeight.Black, color = CyberBlue)
                                }
                                Box(modifier = Modifier.width(1.dp).height(24.dp).background(GlassBorder))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("ORDER COUNT", fontSize = 8.sp, color = CoolGray, fontWeight = FontWeight.Bold)
                                    Text("${allOrders.size} Trans", fontSize = 14.sp, fontWeight = FontWeight.Black, color = SuccessGreen)
                                }
                            }
                        }
                    }
                }

                // Collapsible sections 1: Seller Listings Portfolio with full moderation workflows
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { listingsExpanded = !listingsExpanded }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.DeveloperMode, contentDescription = "l", tint = CyberBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (currentLang == "SI") "මගේ දැන්වීම් කළමනාකරණය (${myProducts.size})" else "Seller Dashboard: My Ads (${myProducts.size})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Icon(
                                imageVector = if (listingsExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = "dropdown"
                            )
                        }

                        AnimatedVisibility(
                            visible = listingsExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                if (myProducts.isEmpty()) {
                                    Text(
                                        text = if (currentLang == "SI") "ඔබ තවම කිසිදු දැන්වීමක් පළ කර නොමැත." else "You have no active listings posted yet. Post items to view details.",
                                        fontSize = 11.sp,
                                        color = CoolGray,
                                        modifier = Modifier.padding(start = 12.dp, bottom = 8.dp)
                                    )
                                } else {
                                    myProducts.forEach { prod ->
                                        GlassCard(isDarkTheme = isDarkTheme, modifier = Modifier.fillMaxWidth()) {
                                            Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(prod.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                                                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                                                            Text("Rs. ${prod.price}", fontSize = 11.sp, color = CyberBlue, fontWeight = FontWeight.Bold)
                                                            Spacer(modifier = Modifier.width(8.dp))
                                                            Icon(Icons.Default.Visibility, contentDescription = "views", tint = SuccessGreen, modifier = Modifier.size(11.dp))
                                                            Spacer(modifier = Modifier.width(2.dp))
                                                            Text("${prod.views} views", fontSize = 10.sp, color = CoolGray)
                                                        }
                                                    }

                                                    // Trash/Mark as sold button
                                                    IconButton(
                                                        onClick = {
                                                            viewModel.removeProductListing(prod.id)
                                                            Toast.makeText(context, "Listing permanently archived/removed!", Toast.LENGTH_SHORT).show()
                                                        },
                                                        modifier = Modifier.size(36.dp)
                                                    ) {
                                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = HotPinkGlow, modifier = Modifier.size(16.dp))
                                                    }
                                                }

                                                Spacer(modifier = Modifier.height(8.dp))

                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    // Status tag (Live vs moderation approval needed)
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        CyberPulseGlow(glowColor = if (prod.isApproved) SuccessGreen else Color(0xFFFFB300), size = 6.dp)
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text(
                                                            text = if (prod.isApproved) "APPROVED & LIVE" else "PENDING SAFETY APPROVAL",
                                                            fontSize = 9.sp,
                                                            color = if (prod.isApproved) SuccessGreen else Color(0xFFFFB300),
                                                            fontWeight = FontWeight.ExtraBold
                                                        )
                                                    }

                                                    // Boost/Feature activator
                                                    if (!prod.isFeatured && prod.isApproved) {
                                                        Button(
                                                            onClick = {
                                                                viewModel.boostProductAd(prod.id)
                                                                Toast.makeText(context, "Ad Successfully Boosted!", Toast.LENGTH_SHORT).show()
                                                            },
                                                            colors = ButtonDefaults.buttonColors(containerColor = HotPinkGlow),
                                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                            modifier = Modifier.height(26.dp)
                                                        ) {
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Icon(Icons.Default.TrendingUp, contentDescription = "g", tint = Color.White, modifier = Modifier.size(10.dp))
                                                                Spacer(modifier = Modifier.width(4.dp))
                                                                Text("Boost Ad", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                                            }
                                                        }
                                                    } else if (prod.isFeatured) {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Icon(Icons.Default.Star, contentDescription = "f", tint = HotPinkGlow, modifier = Modifier.size(12.dp))
                                                            Spacer(modifier = Modifier.width(2.dp))
                                                            Text("Boosted Active", fontSize = 9.sp, color = HotPinkGlow, fontWeight = FontWeight.Bold)
                                                        }
                                                    }
                                                }

                                                // If unapproved, show help tips to approve via Admin
                                                if (!prod.isApproved) {
                                                    Spacer(modifier = Modifier.height(6.dp))
                                                    Button(
                                                        onClick = { viewModel.activeScreen.value = "admin" },
                                                        colors = ButtonDefaults.buttonColors(containerColor = SapphireDark),
                                                        modifier = Modifier.fillMaxWidth().height(28.dp),
                                                        contentPadding = PaddingValues(0.dp)
                                                    ) {
                                                        Text("👉 Sandbox Queue: Open Admin Panel to Approve this Ad", fontSize = 8.sp, color = CyberBlue, fontWeight = FontWeight.SemiBold)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Collapsible sections 2: Buyer Dashboard - Saved Search Monitors
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { savedSearchesExpanded = !savedSearchesExpanded }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Bookmark, contentDescription = "saved", tint = CyberBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (currentLang == "SI") "මගේ සුරැකූ සෙවීම් (${savedSearches.size})" else "Buyer: Saved Searches (${savedSearches.size})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Icon(
                                imageVector = if (savedSearchesExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = "dropdown"
                            )
                        }

                        AnimatedVisibility(
                            visible = savedSearchesExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                if (savedSearches.isEmpty()) {
                                    Text("No saved search alerts monitored.", fontSize = 11.sp, color = CoolGray, modifier = Modifier.padding(start = 12.dp))
                                } else {
                                    savedSearches.forEach { query ->
                                        GlassCard(isDarkTheme = isDarkTheme, modifier = Modifier.fillMaxWidth()) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.weight(1f).clickable {
                                                        viewModel.searchQuery.value = query
                                                        viewModel.activeScreen.value = "home"
                                                        Toast.makeText(context, "Query loading: $query", Toast.LENGTH_SHORT).show()
                                                    }
                                                ) {
                                                    Icon(Icons.Default.History, contentDescription = "h", tint = CyberBlue, modifier = Modifier.size(14.dp))
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(query, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                                                }

                                                IconButton(
                                                    onClick = { viewModel.deleteSavedSearch(query) },
                                                    modifier = Modifier.size(28.dp)
                                                ) {
                                                    Icon(Icons.Default.Delete, contentDescription = "del", tint = HotPinkGlow, modifier = Modifier.size(14.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Collapsible sections 3: Buyer Dashboard - Recently Viewed Items
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { recentlyExpanded = !recentlyExpanded }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.History, contentDescription = "recent", tint = CyberBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (currentLang == "SI") "පසුගිය නැරඹුම් (${recentlyViewed.size})" else "Buyer: Recently Viewed (${recentlyViewed.size})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Icon(
                                imageVector = if (recentlyExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = "dropdown"
                            )
                        }

                        AnimatedVisibility(
                            visible = recentlyExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            if (recentlyViewed.isEmpty()) {
                                Text(
                                    text = "Your view footprints are currently empty.",
                                    fontSize = 11.sp,
                                    color = CoolGray,
                                    modifier = Modifier.padding(start = 12.dp, bottom = 8.dp)
                                )
                            } else {
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(recentlyViewed) { prod ->
                                        Box(
                                            modifier = Modifier
                                                .width(130.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(SapphireDark.copy(alpha = 0.6f))
                                                .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                                                .clickable {
                                                    viewModel.activeProductDetailId.value = prod.id
                                                    viewModel.activeScreen.value = "details"
                                                }
                                                .padding(8.dp)
                                        ) {
                                            Column {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(70.dp)
                                                        .background(ElectricBlue.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = prod.category.take(3).uppercase(),
                                                        color = CyberBlue,
                                                        fontWeight = FontWeight.Black,
                                                        fontSize = 14.sp
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = prod.title,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1,
                                                    color = Color.White
                                                )
                                                Text(
                                                    text = "Rs. ${prod.price}",
                                                    fontSize = 9.sp,
                                                    color = CyberBlue,
                                                    fontWeight = FontWeight.ExtraBold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Collapsible sections 4: Purchase History ledger logs
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { purchasesExpanded = !purchasesExpanded }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ReceiptLong, contentDescription = "o", tint = CyberBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (currentLang == "SI") "මගේ මිලදී ගැනීම් සහ ඇණවුම් (${allOrders.size})" else "Buyer Ledger: Orders & Bids (${allOrders.size})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Icon(
                                imageVector = if (purchasesExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = "dropdown"
                            )
                        }

                        AnimatedVisibility(
                            visible = purchasesExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                if (allOrders.isEmpty()) {
                                    Text("You have not initiated any purchases yet.", fontSize = 11.sp, color = CoolGray, modifier = Modifier.padding(start = 12.dp))
                                } else {
                                    allOrders.forEach { ord ->
                                        GlassCard(isDarkTheme = isDarkTheme, modifier = Modifier.fillMaxWidth()) {
                                            Column(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(ord.id, fontSize = 10.sp, fontWeight = FontWeight.Black, color = SuccessGreen)
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        CyberPulseGlow(glowColor = if (ord.status == "COMPLETED") SuccessGreen else Color(0xFFFFB300), size = 4.dp)
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(ord.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (ord.status == "COMPLETED") SuccessGreen else Color(0xFFFFB300))
                                                    }
                                                }
                                                Text(ord.productTitle, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                                                Row(
                                                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text("Seller: ${ord.sellerName}", fontSize = 10.sp, color = CoolGray)
                                                    Text("Rs. ${ord.price} • ${ord.paymentMethod}", fontSize = 10.sp, color = CyberBlue, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Collapsible sections 5: Notifications Active logs
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { notifExpanded = !notifExpanded }
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.NotificationsActive, contentDescription = "n", tint = CyberBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (currentLang == "SI") "දැනුම්දීම් සහ ඇඟවීම් (${notifications.size})" else "Monitored Alerts (${notifications.size})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Icon(
                                imageVector = if (notifExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = "dropdown"
                            )
                        }

                        AnimatedVisibility(
                            visible = notifExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                if (notifications.isEmpty()) {
                                    Text("Alert boxes are clear.", fontSize = 11.sp, color = CoolGray, modifier = Modifier.padding(start = 12.dp))
                                } else {
                                    notifications.forEach { nm ->
                                        GlassCard(isDarkTheme = isDarkTheme, modifier = Modifier.fillMaxWidth()) {
                                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)) {
                                                CyberPulseGlow(
                                                    glowColor = when (nm.type) {
                                                        "CHAT" -> CyberBlue
                                                        "APPROVAL" -> SuccessGreen
                                                        else -> HotPinkGlow
                                                    },
                                                    size = 4.dp
                                                )
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Text(nm.title, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                                                    Text(nm.content, fontSize = 10.sp, color = CoolGray)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Logout Action block
                item {
                    Button(
                        onClick = { viewModel.logout() },
                        colors = ButtonDefaults.buttonColors(containerColor = HotPinkGlow),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("logout_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Logout, contentDescription = "logout", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("TERMINATE SYSTEM IDENTITY ACCESS SESSION (LOGOUT)", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
