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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardVoice
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import com.example.data.model.ChatMessageEntity
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.CoolGray
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.HotPinkGlow
import com.example.ui.theme.SapphireDark
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodel.MarketplaceViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    viewModel: MarketplaceViewModel,
    isDarkTheme: Boolean = true
) {
    val context = LocalContext.current
    val threadId by viewModel.activeChatThreadId.collectAsState()
    val recipientName by viewModel.activeChatRecipient.collectAsState()
    val messages by viewModel.allMessages.collectAsState()

    var textInput by remember { mutableStateOf("") }
    var isRecordingVo by remember { mutableStateOf(false) }

    val filteredMessages = messages.filter { it.threadId == threadId }
    val listState = rememberLazyListState()

    // Scroll automatically on message sizes change
    LaunchedEffect(filteredMessages.size) {
        if (filteredMessages.isNotEmpty()) {
            listState.animateScrollToItem(filteredMessages.size - 1)
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            // Interactive header panel with seller online indices and call options
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                isDarkTheme = isDarkTheme
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.activeScreen.value = "chat_list" }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back", tint = Color.White)
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // User Identity details
                    Box(modifier = Modifier.size(36.dp).background(ElectricBlue, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = "user avatar", tint = Color.White)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = recipientName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CyberPulseGlow(size = 3.dp, glowColor = SuccessGreen)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Active Node Online", fontSize = 9.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                        }
                    }

                    Row {
                        IconButton(onClick = { Toast.makeText(context, "Initializing visual hologram call stream...", Toast.LENGTH_SHORT).show() }) {
                            Icon(Icons.Default.Phone, contentDescription = "call", tint = CyberBlue, modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.MoreVert, contentDescription = "options", tint = CoolGray, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        CyberBackdrop(modifier = Modifier.fillMaxSize(), isDarkTheme = isDarkTheme) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Main messages node flow list
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredMessages) { msg ->
                        ChatMessageBubble(
                            message = msg,
                            isDarkTheme = isDarkTheme,
                            translateUnit = viewModel
                        )
                    }
                }

                // Voice recording visual helper
                AnimatedVisibility(
                    visible = isRecordingVo,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(HotPinkGlow.copy(alpha = 0.1f))
                            .border(1.dp, HotPinkGlow)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CyberPulseGlow(glowColor = HotPinkGlow, size = 6.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("STREAM RECORDING ACTIVE (Voice Note waveform calibrating)", color = HotPinkGlow, fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }

                // Chat Input box with multiple configurations
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    isDarkTheme = isDarkTheme
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Image file attach icon
                        IconButton(onClick = {
                            viewModel.sendChatMessage("Shared listing attachment item snapshot", "IMAGE", "market_image_payload")
                            Toast.makeText(context, "Translucent image snapshot attached successfully", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.Image, contentDescription = "Attach image button", tint = CyberBlue, modifier = Modifier.size(20.dp))
                        }

                        // Microphone icon
                        IconButton(
                            onClick = {
                                isRecordingVo = !isRecordingVo
                                if (!isRecordingVo) {
                                    // Complete and send voice note
                                    viewModel.sendChatMessage("Voice Note (9.4s Audio Record Stream)", "VOICE", "voice_node_track")
                                    Toast.makeText(context, "Audio sound note successfully published to Room database.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardVoice,
                                contentDescription = "Voice note icon",
                                tint = if (isRecordingVo) HotPinkGlow else CyberBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // Text Field Input
                        OutlinedTextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            placeholder = { Text("Write chat prompt...", fontSize = 11.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("message_input"),
                            singleLine = true,
                            colors = getTextFieldColors(isDarkTheme),
                            shape = RoundedCornerShape(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Send CTA
                        IconButton(
                            onClick = {
                                if (textInput.isNotEmpty()) {
                                    viewModel.sendChatMessage(textInput, "TEXT")
                                    textInput = ""
                                }
                            },
                            modifier = Modifier
                                .background(Brush.horizontalGradient(listOf(ElectricBlue, CyberBlue)), CircleShape)
                                .size(36.dp)
                                .testTag("send_message_button")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send text message", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageBubble(
    message: ChatMessageEntity,
    isDarkTheme: Boolean,
    translateUnit: MarketplaceViewModel
) {
    val isMe = message.senderId == "me"
    val timeFormatted = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp,
                            bottomEnd = if (isMe) 0.dp else 14.dp,
                            bottomStart = if (isMe) 14.dp else 0.dp
                        )
                    )
                    .background(
                        if (isMe) {
                            Brush.linearGradient(listOf(ElectricBlue, Color(0xFF1E3C72)))
                        } else {
                            val bg = if (isDarkTheme) SapphireDark else Color(0x1F000000)
                            Brush.linearGradient(listOf(bg, bg))
                        }
                    )
                    .border(
                        1.dp,
                        if (isMe) CyberBlue.copy(alpha = 0.6f) else GlassBorder,
                        shape = RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp,
                            bottomEnd = if (isMe) 0.dp else 14.dp,
                            bottomStart = if (isMe) 14.dp else 0.dp
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                // Evaluate message content configurations
                Column {
                    when (message.messageType) {
                        "TEXT" -> {
                            Text(
                                text = message.textContent,
                                fontSize = 12.sp,
                                color = Color.White,
                                lineHeight = 16.sp
                            )
                        }

                        "IMAGE" -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Image, contentDescription = "i", tint = CyberBlue, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Image Payload", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CyberBlue)
                                }
                                Text(message.textContent, fontSize = 11.sp, color = Color.LightGray, modifier = Modifier.padding(top = 4.dp))
                            }
                        }

                        "VOICE" -> {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AudioFile, contentDescription = "v", tint = CyberBlue, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text("Sound Note File Stream", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    // Visual audio graph simulation
                                    Text("|||⑾|||⑿||| 9.4s", fontSize = 10.sp, color = CyberBlue, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }
            }

            // Timestamp details row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
            ) {
                Text(
                    text = timeFormatted,
                    fontSize = 8.sp,
                    color = CoolGray
                )
                if (isMe) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "• " + translateUnit.translate("chat_seen"),
                        fontSize = 8.sp,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
