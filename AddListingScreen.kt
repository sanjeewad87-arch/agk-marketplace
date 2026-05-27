package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import com.example.ui.theme.CoolGray
import com.example.ui.theme.SuccessGreen
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.HotPinkGlow
import com.example.ui.theme.SapphireDark
import com.example.ui.viewmodel.MarketplaceViewModel

@Composable
fun AddListingScreen(
    viewModel: MarketplaceViewModel,
    isDarkTheme: Boolean = true
) {
    val context = LocalContext.current

    // Inputs
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Mobiles") }
    var condition by remember { mutableStateOf("New") } // New, Used
    var priceInput by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var locationInput by remember { mutableStateOf("") }
    var mockImagesList by remember { mutableStateOf<List<String>>(emptyList()) }

    // Loader checks
    val isGenPlaying by viewModel.isGeneratingDesc.collectAsState()
    val isPricePlaying by viewModel.isGeneratingPrice.collectAsState()

    val categoriesList = listOf("Mobiles", "Vehicles", "Electronics", "Fashion", "Property", "Services", "Jobs")

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "NEW MARKET CLASSIFIED",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                    Text(
                        text = "AI COGNITIVE TRANSLATION CHANNELS",
                        fontSize = 9.sp,
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
                // Image gallery simulation row
                item {
                    Text(
                        text = "Upload Visual Media assets (Camera / Gallery)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Camera Button placeholder
                        GlassCard(
                            modifier = Modifier
                                .size(80.dp)
                                .clickable {
                                    mockImagesList = mockImagesList + "ad_image_${mockImagesList.size + 1}"
                                    Toast
                                        .makeText(context, "Simulating camera attachment asset stream...", Toast.LENGTH_SHORT)
                                        .show()
                                },
                            isDarkTheme = isDarkTheme
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Image,
                                        contentDescription = "Attach photo",
                                        tint = CyberBlue,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Add Assets", fontSize = 8.sp, color = CoolGray)
                                }
                            }
                        }

                        // Added visual nodes
                        mockImagesList.forEachIndexed { num, node ->
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SapphireDark)
                                    .border(1.dp, CyberBlue, shape = RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "g", tint = CyberBlue, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("Image #$num", fontSize = 8.sp, color = Color.White)
                                }
                            }
                        }
                    }
                }

                // Title Input
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Product / Listing Title") },
                        placeholder = { Text("e.g. Samsung S24 Ultra Singapore 512GB") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = getTextFieldColors(isDarkTheme)
                    )
                }

                // Category selector badge row
                item {
                    Text(
                        text = "Select Category Sector",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoolGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categoriesList) { cat ->
                            val isSelected = category == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) ElectricBlue else Color.Transparent)
                                    .border(1.dp, if (isSelected) CyberBlue else GlassBorder, shape = RoundedCornerShape(14.dp))
                                    .clickable { category = cat }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else if (isDarkTheme) Color.White.copy(alpha = 0.7f) else Color.Black
                                )
                            }
                        }
                    }
                }

                // Condition Check (New / Used)
                item {
                    Text(
                        text = "Overall Item Condition",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoolGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        listOf("New", "Used").forEach { cond ->
                            val isSelected = condition == cond
                            GlassCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { condition = cond },
                                cornerRadius = 12.dp,
                                borderColor = if (isSelected) CyberBlue else GlassBorder,
                                isDarkTheme = isDarkTheme
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CyberPulseGlow(
                                        size = 4.dp,
                                        glowColor = if (isSelected) CyberBlue else Color.Transparent
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (cond == "New") viewModel.translate("condition_new") else viewModel.translate("condition_used"),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (isSelected) Color.White else CoolGray
                                    )
                                }
                            }
                        }
                    }
                }

                // AI Product Description generation field with smart animation
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Item Listing Description",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CoolGray
                            )

                            // AI Autocomplete Spark Button
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Brush.horizontalGradient(listOf(ElectricBlue, CyberBlue)))
                                    .clickable {
                                        if (title.isEmpty()) {
                                            Toast
                                                .makeText(context, "Specify listing title identifier first!", Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            viewModel.generateAiDescriptionForListing(title, category, condition) { desc ->
                                                description = desc
                                            }
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "AI description icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = viewModel.translate("gen_desc_btn"),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = { Text("Describe details of wear, key variables, shipping guidelines...", fontSize = 11.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            maxLines = 6,
                            colors = getTextFieldColors(isDarkTheme)
                        )

                        AnimatedVisibility(visible = isGenPlaying) {
                            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                                LinearProgressIndicator(color = CyberBlue, trackColor = SapphireDark, modifier = Modifier.fillMaxWidth())
                                Text("Gemini AI is crafting copy description parameters...", color = CyberBlue, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Price Input with accompanying AI suggestion
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Classified Retail Value (USD / $)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CoolGray
                            )

                            // AI Price sparkle estimator
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SapphireDark.copy(alpha = 0.5f))
                                    .border(1.dp, CyberBlue, shape = RoundedCornerShape(12.dp))
                                    .clickable {
                                        if (title.isEmpty()) {
                                            Toast
                                                .makeText(context, "Specify title identifier first!", Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            viewModel.generateAiPriceEstimate(title, category, condition) { sug ->
                                                priceInput = String.format("%.2f", sug)
                                            }
                                        }
                                    }
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = "AI appraisal icon",
                                    tint = CyberBlue,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = viewModel.translate("price_suggest"),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CyberBlue
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = priceInput,
                            onValueChange = { priceInput = it },
                            placeholder = { Text("0.00") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = { Icon(Icons.Default.LocalOffer, contentDescription = "price icon", tint = CoolGray) },
                            colors = getTextFieldColors(isDarkTheme)
                        )

                        AnimatedVisibility(visible = isPricePlaying) {
                            Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                                LinearProgressIndicator(color = CyberBlue, trackColor = SapphireDark)
                                Text("Gemini evaluating historic trade benchmarks...", color = CyberBlue, fontSize = 9.sp)
                            }
                        }
                    }
                }

                // GPS Location Selector with Manual coordinates option
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dispatch Location Hub",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CoolGray
                            )

                            // Quick GPS Selector
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        locationInput = "Galle Fort, Southern Sri Lanka"
                                        Toast
                                            .makeText(context, "Geo-Location coordinate fetched successfully!", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    .padding(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.GpsFixed, contentDescription = "GPS", tint = CyberBlue, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("GPS Sync", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberBlue)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = locationInput,
                            onValueChange = { locationInput = it },
                            placeholder = { Text("e.g. Colombo 03, Kandy, Galle Fort") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "g", tint = CoolGray) },
                            colors = getTextFieldColors(isDarkTheme)
                        )
                    }
                }

                // Warning / security tip on publication rules
                item {
                    GlassCard(borderColor = GlassBorder, isDarkTheme = isDarkTheme) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Icon(Icons.Default.ErrorOutline, contentDescription = "Error icon", tint = HotPinkGlow, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "All channels conform to administrative guidelines. Fake listings, unauthorized pricing, or illegal items are blacklisted automatically.",
                                fontSize = 10.sp,
                                color = CoolGray
                            )
                        }
                    }
                }

                // Submit Classified Action
                item {
                    Button(
                        onClick = {
                            if (title.isEmpty() || description.isEmpty() || priceInput.isEmpty() || locationInput.isEmpty()) {
                                Toast
                                    .makeText(context, "All parameters must be successfully configured!", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                val price = priceInput.toDoubleOrNull() ?: 0.0
                                val imgs = if (mockImagesList.isEmpty()) "default_ad" else mockImagesList.joinToString(",")
                                viewModel.createListing(title, description, price, category, condition, locationInput, imgs)

                                Toast.makeText(context, "Listed Classified Successfully!", Toast.LENGTH_LONG).show()

                                // Sync back to Home
                                viewModel.activeScreen.value = "home"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("publish_listing_button")
                    ) {
                        Text("PUBLISH LISTING TO FEED", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
