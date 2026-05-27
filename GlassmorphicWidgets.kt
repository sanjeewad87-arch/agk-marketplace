package com.example.ui.screens

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CyberBlue
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.SapphireDark
import com.example.ui.theme.CoolGray
import androidx.compose.material3.OutlinedTextFieldDefaults

/**
 * Custom glassmorphism layout modifier to draw high density translucent cards.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 1.dp,
    borderColor: Color = GlassBorder,
    isDarkTheme: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val bgColors = if (isDarkTheme) {
        listOf(
            Color(0x3300E5FF), // Subtle cyber glow top
            Color(0x130D111A), // Translucent deep background
            Color(0x280D111A)
        )
    } else {
        listOf(
            Color(0xE6FFFFFF), // Semi-solid white and skyblue tints for clean light card
            Color(0xCDEDF3FC)
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(Brush.verticalGradient(bgColors))
            .border(
                BorderStroke(borderWidth, borderColor),
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(12.dp)
    ) {
        content()
    }
}

/**
 * Futuristic background gradient backdrop
 */
@Composable
fun CyberBackdrop(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "backdrop_animation")
    val animOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "anim_offset"
    )

    Box(
        modifier = modifier
            .background(
                if (isDarkTheme) {
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0x1F00E5FF), // Subtle neon blue aura
                            Color(0x122979FF),
                            Color(0xFF07090E)  // Dark void background
                        ),
                        center = Offset(animOffset - 200f, animOffset / 2),
                        radius = 1800f
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE3EBF6),
                            Color(0xFFF1F4F9)
                        )
                    )
                }
            )
    ) {
        content()
    }
}

/**
 * Pulsing neon status indicator badge (e.g., product boosting, active listing, seen)
 */
@Composable
fun CyberPulseGlow(
    modifier: Modifier = Modifier,
    glowColor: Color = CyberBlue,
    size: Dp = 8.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_animation")
    val animatedAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    val animatedRadius by infiniteTransition.animateFloat(
        initialValue = size.value,
        targetValue = size.value * 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_radius"
    )

    Box(
        modifier = modifier
            .size(size * 3)
            .drawBehind {
                drawCircle(
                    color = glowColor.copy(alpha = animatedAlpha * 0.3f),
                    radius = animatedRadius.dp.toPx()
                )
                drawCircle(
                    color = glowColor,
                    radius = size.toPx()
                )
            },
        contentAlignment = Alignment.Center
    ) {}
}

/**
 * Returns suitable icon for categories
 */
fun getCategoryIcon(category: String): ImageVector {
    return when (category) {
        "Mobiles" -> Icons.Default.Smartphone
        "Vehicles" -> Icons.Default.DirectionsCar
        "Electronics" -> Icons.Default.ElectricalServices
        "Fashion" -> Icons.Default.Build // Use available Icons or core ones
        "Property" -> Icons.Default.HomeWork
        "Services" -> Icons.Default.FitnessCenter
        "Jobs" -> Icons.Default.BusinessCenter
        else -> Icons.Default.Category
    }
}

/**
 * Common text field colors generator mapped to futuristic aesthetics
 */
@Composable
fun getTextFieldColors(isDarkTheme: Boolean) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = CyberBlue,
    unfocusedBorderColor = if (isDarkTheme) Color.White.copy(alpha = 0.15f) else Color.DarkGray.copy(alpha = 0.25f),
    focusedLabelColor = CyberBlue,
    unfocusedLabelColor = if (isDarkTheme) Color.White.copy(alpha = 0.5f) else Color.DarkGray.copy(alpha = 0.6f),
    focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
    unfocusedTextColor = if (isDarkTheme) Color.White.copy(alpha = 0.8f) else Color.Black,
    focusedContainerColor = if (isDarkTheme) SapphireDark.copy(alpha = 0.4f) else Color.White,
    unfocusedContainerColor = if (isDarkTheme) SapphireDark.copy(alpha = 0.4f) else Color.White
)

/**
 * Animated Gemini spinner overlay
 */
@Composable
fun ShinyGeminiLoader(
    text: String = "AI Gemini calibrating...",
    isDarkTheme: Boolean = true
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .testTag("ai_loading_spinner")
            .background(if (isDarkTheme) Color(0x66000000) else Color(0x66FFFFFF), shape = RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        GlassCard(
            modifier = Modifier.padding(16.dp),
            borderColor = CyberBlue.copy(alpha = 0.5f),
            isDarkTheme = isDarkTheme
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(8.dp)) {
                CircularProgressIndicator(
                    modifier = Modifier.size(54.dp),
                    color = CyberBlue,
                    strokeWidth = 4.dp
                )
            }
        }
    }
}
