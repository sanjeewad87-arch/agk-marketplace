package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MarkChatRead
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.HotPinkGlow
import com.example.ui.theme.SapphireDark
import com.example.ui.theme.CoolGray
import com.example.ui.theme.GlassBorder
import com.example.ui.viewmodel.MarketplaceViewModel

@Composable
fun ChatListScreen(
    viewModel: MarketplaceViewModel,
    isDarkTheme: Boolean = true
) {
    val messages by viewModel.allMessages.collectAsState()

    // Distinct threads extractor
    val threadsList = messages.groupBy { it.threadId }.values.map { list ->
        val lastMsg = list.maxByOrNull { it.timestamp }!!
        val senderTitle = if (lastMsg.senderId == "me") lastMsg.receiverId else lastMsg.senderId
        ThreadItem(
            threadId = lastMsg.threadId,
            recipientName = senderTitle,
            lastMessage = lastMsg.textContent,
            timestamp = lastMsg.timestamp,
            hasUnread = list.any { !it.isSeen && it.senderId != "me" }
        )
    }.sortedByDescending { it.timestamp }

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
                        text = viewModel.translate("chats"),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                    Text(
                        text = "SECURE SYMMETRIC CHAT TUNNEL",
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
            if (threadsList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.ChatBubbleOutline,
                        contentDescription = "Empty secure inbox",
                        tint = CoolGray,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Inbox completely clear.",
                        color = CoolGray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Initiate chats on details cards.",
                        color = CyberBlue,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(threadsList) { th ->
                        GlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.selectChatThread(th.threadId, th.recipientName)
                                },
                            isDarkTheme = isDarkTheme
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Profile Frame
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clickable {}
                                        .background(ElectricBlue, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = "r", tint = Color.White)
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = th.recipientName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (isDarkTheme) Color.White else Color.Black
                                        )

                                        if (th.hasUnread) {
                                            CyberPulseGlow(glowColor = HotPinkGlow, size = 5.dp)
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.MarkChatRead,
                                                contentDescription = "Read ticker icon",
                                                tint = CyberBlue,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = th.lastMessage,
                                        fontSize = 11.sp,
                                        color = if (th.hasUnread) Color.White else CoolGray,
                                        fontWeight = if (th.hasUnread) FontWeight.Bold else FontWeight.Normal,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Icon(Icons.Default.ChevronRight, contentDescription = "Arrow right icon", tint = CoolGray, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

data class ThreadItem(
    val threadId: String,
    val recipientName: String,
    val lastMessage: String,
    val timestamp: Long,
    val hasUnread: Boolean
)
