package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProductEntity
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.CoolGray
import com.example.ui.theme.HotPinkGlow
import com.example.ui.theme.SapphireDark
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.MarketplaceViewModel

@Composable
fun ProductDetailsScreen(
    viewModel: MarketplaceViewModel,
    isDarkTheme: Boolean = true
) {
    val context = LocalContext.current
    val productId = viewModel.activeProductDetailId.collectAsState().value
    val allProducts by viewModel.allProducts.collectAsState()
    val wishlistItems by viewModel.wishlistedProducts.collectAsState()
    val currentLang by viewModel.activeLanguage.collectAsState()

    var product by remember { mutableStateOf<ProductEntity?>(null) }
    var offerPriceInput by remember { mutableStateOf("") }

    // Synchronize selected item
    LaunchedEffect(productId, allProducts) {
        if (productId != null) {
            val found = allProducts.find { it.id == productId }
            product = found
            found?.let { viewModel.addToRecentlyViewed(it) }
        }
    }

    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading catalog parameters...", color = CyberBlue)
        }
        return
    }

    val item = product!!
    val isWishlisted = wishlistItems.any { it.id == item.id }

    // Similar ads logic
    val similarAds = allProducts.filter { it.category == item.category && it.id != item.id }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            // Action button bar (Call, Chat, Buy)
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                isDarkTheme = isDarkTheme
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Call Seller
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${item.sellerPhone}")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .background(SapphireDark, CircleShape)
                            .border(1.dp, CyberBlue, CircleShape)
                            .size(48.dp)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "call seller", tint = CyberBlue)
                    }

                    // Start Chat
                    IconButton(
                        onClick = {
                            // Unique thread hash combining buyer and seller
                            val threadId = "chat_${item.id}_${item.sellerName.replace(" ", "_")}"
                            viewModel.selectChatThread(threadId, item.sellerName)
                        },
                        modifier = Modifier
                            .background(SapphireDark, CircleShape)
                            .border(1.dp, CyberBlue, CircleShape)
                            .size(48.dp)
                            .testTag("chat_seller_button")
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = "chat seller", tint = CyberBlue)
                    }

                    // Checkout CTA Launcher
                    Button(
                        onClick = {
                            viewModel.activeScreen.value = "checkout"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("buy_now_button"),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ElectricBolt, contentDescription = "b", tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("LOCK DEAL & SECURE CHECKOUT", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        CyberBackdrop(modifier = Modifier.fillMaxSize(), isDarkTheme = isDarkTheme) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                // Header navigation & actions Row
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.activeScreen.value = "home" },
                            modifier = Modifier.background(SapphireDark.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back", tint = Color.White)
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Wishlist Toggle
                            IconButton(
                                onClick = { viewModel.toggleProductWishlist(item.id) },
                                modifier = Modifier.background(SapphireDark.copy(alpha = 0.5f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "fav",
                                    tint = if (isWishlisted) HotPinkGlow else Color.White
                                )
                            }

                            // Share Action
                            IconButton(
                                onClick = {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TITLE, item.title)
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "Check out this premium ${item.category} listed on AGK Marketplace: ${item.title} for ${viewModel.formatPrice(item.price)}. Highly recommended deal in ${item.location}!"
                                        )
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Listing Parameters"))
                                },
                                modifier = Modifier.background(SapphireDark.copy(alpha = 0.5f), CircleShape)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "share", tint = Color.White)
                            }
                        }
                    }
                }

                // Full visual gallery carousel hero banner
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(SapphireDark, Color.Black)
                                )
                            )
                            .border(1.dp, GlassBorder, RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = getCategoryIcon(item.category),
                                contentDescription = "category preview",
                                tint = CyberBlue,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            // Glowing badge
                            if (item.isFeatured) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(HotPinkGlow)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text("HIGH PROFILE CHANNELS", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White)
                                }
                            }
                        }
                    }
                }

                // Product pricing & titles details
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.category.uppercase(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = CyberBlue,
                                letterSpacing = 1.sp
                            )

                            // Condition Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (item.condition.lowercase() == "new") SuccessGreen.copy(alpha = 0.15f) else Color.Transparent)
                                    .border(1.dp, if (item.condition.lowercase() == "new") SuccessGreen else GlassBorder, shape = RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (item.condition.lowercase() == "new") "Brand New" else "Used / Great Condition",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.condition.lowercase() == "new") SuccessGreen else Color.White
                                )
                            }
                        }

                        Text(
                            text = item.title,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            color = if (isDarkTheme) Color.White else Color.Black,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Text(
                            text = viewModel.formatPrice(item.price),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDarkTheme) CyberBlue else ElectricBlue,
                            modifier = Modifier.padding(top = 6.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = "g", tint = CyberBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(item.location, fontSize = 12.sp, color = CoolGray)
                        }
                    }
                }

                // Product written description parameters
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Item details",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        GlassCard(isDarkTheme = isDarkTheme) {
                            Text(
                                text = item.description,
                                fontSize = 12.sp,
                                color = if (isDarkTheme) Color.White.copy(alpha = 0.82f) else Color.DarkGray,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }

                // Sinhala and Multilingual Safety Notice Card
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        GlassCard(
                            isDarkTheme = isDarkTheme,
                            borderColor = HotPinkGlow.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(6.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    CyberPulseGlow(glowColor = HotPinkGlow, size = 6.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = viewModel.translate("safety_warning_title"),
                                        fontWeight = FontWeight.Black,
                                        fontSize = 12.sp,
                                        color = HotPinkGlow,
                                        letterSpacing = 1.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // BOLD Sinhala warning as explicitly requested
                                Text(
                                    text = "භාණ්ඩ පරීක්ෂා කිරීමකින් තොරව මුදල් ගෙවීම් නොකරන්න",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .background(HotPinkGlow.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp))
                                        .padding(8.dp)
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = viewModel.translate("safety_warning_desc"),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.LightGray
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = viewModel.translate("safety_item_1"),
                                        fontSize = 10.sp,
                                        color = CoolGray
                                    )
                                    Text(
                                        text = viewModel.translate("safety_item_2"),
                                        fontSize = 10.sp,
                                        color = CoolGray
                                    )
                                    Text(
                                        text = viewModel.translate("safety_item_3"),
                                        fontSize = 10.sp,
                                        color = CoolGray
                                    )
                                }
                            }
                        }
                    }
                }

                // Price Negotiation and bidding system
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = viewModel.translate("make_offer"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        GlassCard(isDarkTheme = isDarkTheme) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = offerPriceInput,
                                    onValueChange = { offerPriceInput = it },
                                    placeholder = { 
                                        Text(
                                            text = viewModel.translate("make_offer_placeholder"),
                                            fontSize = 11.sp,
                                            color = CoolGray
                                        ) 
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("negotiation_price_input"),
                                    singleLine = true,
                                    colors = getTextFieldColors(isDarkTheme),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Button(
                                    onClick = {
                                        if (offerPriceInput.isNotBlank()) {
                                            val offerText = if (viewModel.activeLanguage.value == "SI") {
                                                "මම මෙම භාණ්ඩයට කැමැත්තෙමි. මාගේ මිල යෝජනාව රු. ${offerPriceInput} වේ. අපට මිල සාකච්ඡා කළ හැකිද?"
                                            } else {
                                                "I am interested in '${item.title}'. I would like to offer Rs. ${offerPriceInput}. Is this price negotiable?"
                                            }
                                            // Select Chat thread and launch message dispatching
                                            val threadId = "chat_${item.id}_${item.sellerName.replace(" ", "_")}"
                                            viewModel.selectChatThread(threadId, item.sellerName)
                                            viewModel.sendChatMessage(offerText, "TEXT")
                                            Toast.makeText(context, "Price negotiation dispatched to Seller Inbox!", Toast.LENGTH_LONG).show()
                                            offerPriceInput = ""
                                        } else {
                                            Toast.makeText(context, "Please enter a valid amount first", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberBlue),
                                    modifier = Modifier
                                        .height(44.dp)
                                        .testTag("submit_negotiation_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Chat, contentDescription = "chat", tint = Color.White, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Send", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // Seller specifications and verified tags
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                    ) {
                        Text(
                            text = "Merchant profile details",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        GlassCard(
                            isDarkTheme = isDarkTheme,
                            borderColor = CyberBlue.copy(alpha = 0.25f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Avatar frame
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(ElectricBlue, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = "avatar", tint = Color.White)
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = item.sellerName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (isDarkTheme) Color.White else Color.Black
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.VerifiedUser, contentDescription = "v", tint = CyberBlue, modifier = Modifier.size(12.dp))
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Star, contentDescription = "r", tint = HotPinkGlow, modifier = Modifier.size(10.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = "${item.sellerRating} / 5.0 rating index",
                                            fontSize = 9.sp,
                                            color = CoolGray
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SapphireDark)
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.CardMembership, contentDescription = "ad channel info", tint = CyberBlue, modifier = Modifier.size(10.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Premium Seller", fontSize = 9.sp, color = CyberBlue, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Similar Ads list row
                if (similarAds.isNotEmpty()) {
                    item {
                        Text(
                            text = "Similar ads around you",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDarkTheme) Color.White else Color.Black,
                            modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                        )

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(similarAds) { sim ->
                                GlassCard(
                                    modifier = Modifier
                                        .width(180.dp)
                                        .clickable {
                                            viewModel.activeProductDetailId.value = sim.id
                                        },
                                    isDarkTheme = isDarkTheme
                                ) {
                                    Column {
                                        Icon(
                                            imageVector = getCategoryIcon(sim.category),
                                            contentDescription = "c",
                                            tint = CyberBlue,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            text = sim.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = if (isDarkTheme) Color.White else Color.Black,
                                            maxLines = 1,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                        Text(
                                            text = viewModel.formatPrice(sim.price),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CyberBlue,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
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
