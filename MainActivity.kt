package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.AddListingScreen
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.ChatDetailScreen
import com.example.ui.screens.ChatListScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProductDetailsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.PurchaseScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.CoolGray
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MarketplaceViewModel

class MainActivity : ComponentActivity() {
    
    // Instantiate the ViewModel using standard Android Jetpack viewModels delegate
    private val marketplaceViewModel: MarketplaceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Supports borderless edge-edge drawing as requested in guidelines
        enableEdgeToEdge()
        
        setContent {
            var darkThemeByPreference by remember { mutableStateOf(true) } // Dark Mode is the futuristic default Blue & Black

            MyApplicationTheme(darkTheme = darkThemeByPreference) {
                val loggedIn by marketplaceViewModel.isLoggedIn.collectAsState()
                val currentScreen by marketplaceViewModel.activeScreen.collectAsState()

                if (!loggedIn) {
                    AuthScreen(viewModel = marketplaceViewModel, isDarkTheme = darkThemeByPreference)
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            // Render standard M3 bottom bar when on core tab screens
                            if (currentScreen in listOf("home", "search", "chat_list", "profile")) {
                                AGKBottomNavigationBar(
                                    currentTab = currentScreen,
                                    onTabSelected = { tab ->
                                        marketplaceViewModel.activeScreen.value = tab
                                    },
                                    isDarkTheme = darkThemeByPreference
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            // Unified State-Driven Routing Engine
                            when (currentScreen) {
                                "home" -> HomeScreen(viewModel = marketplaceViewModel, isDarkTheme = darkThemeByPreference)
                                "search" -> SearchScreen(viewModel = marketplaceViewModel, isDarkTheme = darkThemeByPreference)
                                "add" -> AddListingScreen(viewModel = marketplaceViewModel, isDarkTheme = darkThemeByPreference)
                                "chat_list" -> ChatListScreen(viewModel = marketplaceViewModel, isDarkTheme = darkThemeByPreference)
                                "chat_detail" -> ChatDetailScreen(viewModel = marketplaceViewModel, isDarkTheme = darkThemeByPreference)
                                "profile" -> ProfileScreen(
                                    viewModel = marketplaceViewModel,
                                    isDarkTheme = darkThemeByPreference,
                                    onThemeToggle = { target ->
                                        darkThemeByPreference = target
                                    }
                                )
                                "admin" -> AdminPanelScreen(viewModel = marketplaceViewModel, isDarkTheme = darkThemeByPreference)
                                "checkout" -> PurchaseScreen(viewModel = marketplaceViewModel, isDarkTheme = darkThemeByPreference)
                                "details" -> ProductDetailsScreen(viewModel = marketplaceViewModel, isDarkTheme = darkThemeByPreference)
                                else -> HomeScreen(viewModel = marketplaceViewModel, isDarkTheme = darkThemeByPreference)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AGKBottomNavigationBar(
    currentTab: String,
    onTabSelected: (String) -> Unit,
    isDarkTheme: Boolean
) {
    NavigationBar(
        containerColor = if (isDarkTheme) Color(0xFF0D111A) else Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("bottom_navigation_bar")
    ) {
        // Tab 1: Home
        NavigationBarItem(
            selected = currentTab == "home",
            onClick = { onTabSelected("home") },
            icon = {
                Icon(
                    imageVector = if (currentTab == "home") Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home index feed"
                )
            },
            label = { Text("Home", fontSize = 10.sp) },
            colors = getNavBarItemColors(isDarkTheme)
        )

        // Tab 2: Neural NLP Search
        NavigationBarItem(
            selected = currentTab == "search",
            onClick = { onTabSelected("search") },
            icon = {
                Icon(
                    imageVector = if (currentTab == "search") Icons.Filled.Search else Icons.Outlined.Search,
                    contentDescription = "Smart voice search prompt"
                )
            },
            label = { Text("Search", fontSize = 10.sp) },
            colors = getNavBarItemColors(isDarkTheme)
        )

        // Tab 3: Chats Inbox
        NavigationBarItem(
            selected = currentTab == "chat_list" || currentTab == "chat_detail",
            onClick = { onTabSelected("chat_list") },
            icon = {
                Icon(
                    imageVector = if (currentTab == "chat_list") Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "Secure messages threads"
                )
            },
            label = { Text("Inbox", fontSize = 10.sp) },
            colors = getNavBarItemColors(isDarkTheme)
        )

        // Tab 4: Profile & Translation setups
        NavigationBarItem(
            selected = currentTab == "profile",
            onClick = { onTabSelected("profile") },
            icon = {
                Icon(
                    imageVector = if (currentTab == "profile") Icons.Filled.Person else Icons.Outlined.PersonOutline,
                    contentDescription = "User wallet profiles coordinates"
                )
            },
            label = { Text("Profile", fontSize = 10.sp) },
            colors = getNavBarItemColors(isDarkTheme)
        )
    }
}

@Composable
fun getNavBarItemColors(isDarkTheme: Boolean) = NavigationBarItemDefaults.colors(
    selectedIconColor = if (isDarkTheme) CyberBlue else ElectricBlue,
    unselectedIconColor = CoolGray,
    selectedTextColor = if (isDarkTheme) CyberBlue else ElectricBlue,
    unselectedTextColor = CoolGray,
    indicatorColor = if (isDarkTheme) Color(0x3300E5FF) else Color(0x1F2979FF)
)
