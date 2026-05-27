package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SettingsVoice
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.animation.core.tween
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

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    viewModel: MarketplaceViewModel,
    isDarkTheme: Boolean = true
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val allProducts by viewModel.allProducts.collectAsState()

    var queryInput by remember { mutableStateOf("") }
    var voiceSearchActive by remember { mutableStateOf(false) }

    val isSmartSearching by viewModel.isSmartSearching.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val suggestedSearches = listOf(
        "Modern luxury apartments in Colombo",
        "Singapore model iPhone 15 Max",
        "Clean Land Cruiser Prado near Galle Fort",
        "Looking for expert Senior Mobile Architect",
        "Waterproof futurist sports windbreaker"
    )

    // Dynamic Filter on products
    val filteredResults = allProducts.filter { prod ->
        val matchesCategory = selectedCategory == "All" || prod.category == selectedCategory
        val matchesSearch = searchQuery.isEmpty() || prod.title.lowercase().contains(searchQuery.lowercase()) ||
                prod.description.lowercase().contains(searchQuery.lowercase())
        matchesCategory && matchesSearch
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
                Column {
                    Text(
                        text = "NEURAL SEARCH DECK",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                    Text(
                        text = "GEMINI AI NATURAL LANGUAGE DIRECT PROCESSOR",
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Interactive AI Search Input Bar
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = queryInput,
                            onValueChange = { queryInput = it },
                            placeholder = { Text("Enter plain natural request descriptions...", fontSize = 11.sp) },
                            singleLine = true,
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = "search icon", tint = CyberBlue, modifier = Modifier.clickable {
                                    if (queryInput.isNotEmpty()) {
                                        viewModel.triggerSmartSearch(queryInput)
                                    }
                                })
                            },
                            trailingIcon = {
                                if (queryInput.isNotEmpty()) {
                                    IconButton(onClick = { queryInput = "" }) {
                                        Icon(Icons.Default.Cancel, contentDescription = "clear icon", tint = CoolGray)
                                    }
                                }
                            },
                            colors = getTextFieldColors(isDarkTheme),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("smart_search_field")
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        // Voice microphone widget
                        IconButton(
                            onClick = {
                                voiceSearchActive = true
                            },
                            modifier = Modifier
                                .background(Brush.horizontalGradient(listOf(ElectricBlue, CyberBlue)), CircleShape)
                                .size(48.dp)
                                .testTag("voice_search_btn")
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = "Voice prompt search", tint = Color.White)
                        }
                    }
                }

                // AI Processing bar
                item {
                    AnimatedVisibility(visible = isSmartSearching) {
                        Column {
                            LinearProgressIndicator(color = CyberBlue, trackColor = SapphireDark, modifier = Modifier.fillMaxWidth())
                            Text("Gemini decoding semantic entities...", color = CyberBlue, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }

                // AI Suggestion prompts
                item {
                    Text("Semantic prompt suggestions", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CoolGray)
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestedSearches.forEach { hint ->
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SapphireDark.copy(alpha = 0.5f))
                                    .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                                    .clickable {
                                        queryInput = hint
                                        viewModel.triggerSmartSearch(hint)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = "g", tint = CyberBlue, modifier = Modifier.size(10.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(hint, fontSize = 10.sp, color = CyberBlue, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Render matching queries
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Matching Search Coordinates (${filteredResults.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                        if (searchQuery.isNotEmpty() || selectedCategory != "All") {
                            Text(
                                text = "Reset Search Filter",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = HotPinkGlow,
                                modifier = Modifier.clickable {
                                    viewModel.searchQuery.value = ""
                                    viewModel.selectedCategory.value = "All"
                                    queryInput = ""
                                }
                            )
                        }
                    }
                }

                if (filteredResults.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().height(140.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No items located for semantic coordinates.", fontSize = 11.sp, color = CoolGray)
                            Text("Try parsing different keywords.", fontSize = 9.sp, color = CyberBlue)
                        }
                    }
                } else {
                    items(filteredResults) { res ->
                        ProductCardRow(
                            product = res,
                            isWishlisted = false,
                            isDarkTheme = isDarkTheme,
                            onWishlistToggle = {},
                            onClick = {
                                viewModel.activeProductDetailId.value = res.id
                                viewModel.activeScreen.value = "details"
                            }
                        )
                    }
                }
            }
        }

        // VOICE SEARCH MODAL PROMPT OVERLAY
        AnimatedVisibility(
            visible = voiceSearchActive,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                // Interactive micro countdown
                LaunchedEffect(Unit) {
                    delay(3000) // Simulates 3 seconds listening process
                    queryInput = "Luxury Penthouse Suite - Havelock"
                    viewModel.triggerSmartSearch("Luxury Penthouse Suite - Havelock")
                    voiceSearchActive = false
                    Toast.makeText(context, "Voice note resolved successfully by Gemini AI!", Toast.LENGTH_SHORT).show()
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Pulsing micro glow
                    CyberPulseGlow(glowColor = CyberBlue, size = 60.dp)

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = viewModel.translate("voice_search_speak"),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Decoding sound oscillations into category segments in Colombo...",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoolGray
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    IconButton(
                        onClick = { voiceSearchActive = false },
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.1f), CircleShape)
                            .size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Cancel Voice note",
                            tint = HotPinkGlow,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
