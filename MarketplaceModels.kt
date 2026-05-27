package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val price: Double,
    val category: String, // Mobiles, Vehicles, Electronics, Fashion, Property, Services, Jobs
    val condition: String, // New, Used
    val location: String, // GPS or manual input
    val imageUrls: String, // Comma separated list of visual mock URLs or placeholder drawings
    val sellerId: Int = 101,
    val sellerName: String = "Sanjeewa D.",
    val sellerPhone: String = "+94 77 123 4567",
    val sellerRating: Float = 4.8f,
    val sellerAvatar: String = "avatar_user",
    val isApproved: Boolean = true, // For admin approval workflow
    val isFeatured: Boolean = false, // Product boosting
    val views: Int = 24,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val messageId: Int = 0,
    val threadId: String, // combination of buyer_seller or standard unique identifier
    val senderId: String, // "me" or "seller_name"
    val receiverId: String,
    val textContent: String,
    val messageType: String = "TEXT", // TEXT, IMAGE, VOICE
    val mediaUrl: String? = null,
    val voiceDurationSec: Int = 0,
    val isSeen: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "wishlist")
data class WishlistItemEntity(
    @PrimaryKey val productId: Int
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: String = "PROMOTION" // CHAT, ORDER, APPROVAL, PROMOTION
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Sanjeewa D.",
    val email: String = "SanjeewaD87@gmail.com",
    val phone: String = "+94 77 123 4567",
    val bio: String = "Premium AGK Seller from Sri Lanka.",
    val avatarPath: String = "avatar_sanjeeva",
    val rating: Float = 4.9f,
    val joinedDate: String = "May 2026",
    val balanceStripeUSD: Double = 0.0,
    val balancePayHereLKR: Double = 0.0,
    val language: String = "EN" // EN, SI, TA
)
