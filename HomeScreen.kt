package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MarketplaceViewModel,
    isDarkTheme: Boolean = true
) {
    val allProducts by viewModel.allProducts.collectAsState()
    val selectedCat by viewModel.selectedCategory.collectAsState()
    val searchQ by viewModel.searchQuery.collectAsState()
    val gpsNearbyFilter by viewModel.filterNearbyOnly.collectAsState()
    val wishlistItems by viewModel.wishlistedProducts.collectAsState()
    val currentLang by viewModel.activeLanguage.collectAsState()

    var searchQueryInput by remember { mutableStateOf("") }

    val categoriesList = listOf("All", "Mobiles", "Vehicles", "Electronics", "Fashion", "Property", "Services", "Jobs")

    // Filter logic
    val filteredProducts = allProducts.filter { product ->
        val matchesCategory = selectedCat == "All" || product.category == selectedCat
        val matchesSearch = searchQ.isEmpty() || product.title.lowercase().contains(searchQ.lowercase()) ||
                product.description.lowercase().contains(searchQ.lowercase())
        val matchesGps = !gpsNearbyFilter || product.location.lowercase().contains("colombo 03") ||
                product.location.lowercase().contains("colombo 04") || product.location.lowercase().contains("havelock")

        matchesCategory && matchesSearch && matchesGps
    }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.activeScreen.value = "add" },
                containerColor = ElectricBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 60.dp) // Leave safety gap for bottom bar
                    .testTag("add_item_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Publish Ad", modifier = Modifier.size(24.dp))
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
                // AGK header section
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "AGK MARKETPLACE",
                                fontWeight = FontWeight.Black,
                                fontSize = 21.sp,
                                letterSpacing = 1.sp,
                                color = if (isDarkTheme) CyberBlue else ElectricBlue
                            )
                            Text(
                                text = "AI COGNITIVE SECONDARY SYSTEM",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White.copy(alpha = 0.4f) else Color.DarkGray.copy(alpha = 0.6f)
                            )
                        }

                        // Notifications list badge visual
                        Box(contentAlignment = Alignment.Center) {
                            IconButton(onClick = { viewModel.activeScreen.value = "profile" }) {
                                Icon(
                                    Icons.Default.NotificationsActive,
                                    contentDescription = "Alerts Inbox",
                                    tint = if (isDarkTheme) Color.White else Color.Black,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            // Glow dot
                            CyberPulseGlow(
                                glowColor = HotPinkGlow,
                                size = 4.dp,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 8.dp, end = 8.dp)
                            )
                        }
                    }
                }

                // Cyber interactive search bar
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQueryInput,
                            onValueChange = { searchQueryInput = it },
                            placeholder = { Text(viewModel.translate("search_hint"), fontSize = 12.sp) },
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search icon",
                                    tint = CyberBlue,
                                    modifier = Modifier.clickable {
                                        viewModel.searchQuery.value = searchQueryInput
                                    }
                                )
                            },
                            colors = getTextFieldColors(isDarkTheme),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        // GPS Nearby Toggle layout
                        GlassCard(
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    viewModel.filterNearbyOnly.value = !gpsNearbyFilter
                                },
                            cornerRadius = 25.dp,
                            isDarkTheme = isDarkTheme
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(
                                    Icons.Default.GpsFixed,
                                    contentDescription = "GPS Toggle",
                                    tint = if (gpsNearbyFilter) SuccessGreen else if (isDarkTheme) Color.White else Color.Black,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    AnimatedVisibility(visible = gpsNearbyFilter) {
                        Text(
                            text = "ACTIVE GPS RANGE: HIGH-DENSITY CODES IN COLOMBO",
                            color = SuccessGreen,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 24.dp, top = 4.dp)
                        )
                    }
                }

                // Horizontally scrolling sliding trending hotspot banner
                item {
                    Text(
                        text = viewModel.translate("trending"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 0.5.sp,
                        color = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 10.dp)
                    )

                    val trendingAds = allProducts.filter { it.isFeatured }
                    if (trendingAds.isEmpty()) {
                        // Empty state tip
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No live boosted channels active.", fontSize = 12.sp, color = CoolGray)
                        }
                    } else {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(trendingAds) { ad ->
                                GlassCard(
                                    modifier = Modifier
                                        .width(260.dp)
                                        .height(130.dp)
                                        .clickable {
                                            viewModel.activeProductDetailId.value = ad.id
                                            viewModel.activeScreen.value = "details"
                                        },
                                    cornerRadius = 16.dp,
                                    borderColor = HotPinkGlow.copy(alpha = 0.5f),
                                    isDarkTheme = isDarkTheme
                                ) {
                                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Default.LocalFireDepartment,
                                                    contentDescription = "Trending logo",
                                                    tint = HotPinkGlow,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = ad.category.uppercase(),
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = HotPinkGlow
                                                )
                                            }
                                            Text(
                                                text = viewModel.formatPrice(ad.price),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = CyberBlue
                                            )
                                        }

                                        Text(
                                            text = ad.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (isDarkTheme) Color.White else Color.Black,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Text(
                                            text = ad.description,
                                            fontSize = 10.sp,
                                            color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color.DarkGray,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.LocationOn, contentDescription = "g", tint = CyberBlue, modifier = Modifier.size(10.dp))
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(ad.location, fontSize = 9.sp, color = CoolGray)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Dynamic categories chip selector row
                item {
                    Text(
                        text = viewModel.translate("categories"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (isDarkTheme) Color.White else Color.Black,
                        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 10.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categoriesList) { cat ->
                            val isSelected = selectedCat == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        if (isSelected) {
                                            Brush.horizontalGradient(listOf(ElectricBlue, CyberBlue))
                                        } else {
                                            val bg = if (isDarkTheme) Color(0x1F00E5FF) else Color(0x1A000000)
                                            Brush.horizontalGradient(listOf(bg, bg))
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) CyberBlue else GlassBorder,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable {
                                        viewModel.selectedCategory.value = cat
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (cat != "All") {
                                        Icon(
                                            imageVector = getCategoryIcon(cat),
                                            contentDescription = cat,
                                            tint = if (isSelected) Color.White else CyberBlue,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    Text(
                                        text = cat,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else if (isDarkTheme) Color.White.copy(alpha = 0.8f) else Color.Black
                                    )
                                }
                            }
                        }
                    }
                }

                // Grid Recommended Products Title
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = viewModel.translate("recommended"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                        if (selectedCat != "All" || searchQ.isNotEmpty() || gpsNearbyFilter) {
                            Text(
                                text = "Clear filter",
                                fontSize = 11.sp,
                                color = HotPinkGlow,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    viewModel.selectedCategory.value = "All"
                                    viewModel.searchQuery.value = ""
                                    viewModel.filterNearbyOnly.value = false
                                }
                            )
                        }
                    }
                }

                // Grid feed items
                if (filteredProducts.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No compatible listings inside localized coordinates.", fontSize = 12.sp, color = CoolGray)
                            Text("Try searching other regions.", fontSize = 11.sp, color = CyberBlue, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                } else {
                    items(filteredProducts) { item ->
                        ProductCardRow(
                            product = item,
                            isWishlisted = wishlistItems.any { it.id == item.id },
                            isDarkTheme = isDarkTheme,
                            onWishlistToggle = { viewModel.toggleProductWishlist(item.id) },
                            onClick = {
                                viewModel.activeProductDetailId.value = item.id
                                viewModel.activeScreen.value = "details"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCardRow(
    product: ProductEntity,
    isWishlisted: Boolean,
    isDarkTheme: Boolean,
    onWishlistToggle: () -> Unit,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable { onClick() },
        cornerRadius = 16.dp,
        isDarkTheme = isDarkTheme
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Mock Image Container Frame
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                SapphireDark,
                                ElectricBlue.copy(alpha = 0.3f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Generative symbol corresponding to category
                Icon(
                    imageVector = getCategoryIcon(product.category),
                    contentDescription = "t",
                    tint = CyberBlue.copy(alpha = 0.7f),
                    modifier = Modifier.size(36.dp)
                )

                // Boosting channel badge
                if (product.isFeatured) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .background(HotPinkGlow, shape = RoundedCornerShape(topEnd = 8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricBolt,
                            contentDescription = "b",
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details Container
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.category.uppercase(),
                        fontWeight = FontWeight.Black,
                        fontSize = 9.sp,
                        color = CyberBlue,
                        letterSpacing = 0.5.sp
                    )

                    IconButton(
                        onClick = { onWishlistToggle() },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "w",
                            tint = if (isWishlisted) HotPinkGlow else CoolGray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isDarkTheme) Color.White else Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = viewModel.formatPrice(product.price),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isDarkTheme) Color.White else ElectricBlue,
                    modifier = Modifier.padding(top = 1.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = "g", tint = CyberBlue, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(product.location, fontSize = 10.sp, color = CoolGray)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Visibility, contentDescription = "v", tint = CoolGray, modifier = Modifier.size(11.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(product.views.toString(), fontSize = 9.sp, color = CoolGray)
                    }
                }
            }
        }
    }
}
