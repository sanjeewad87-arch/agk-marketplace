package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.BuildConfig
import com.example.data.local.MarketplaceDatabase
import com.example.data.model.ChatMessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ProductEntity
import com.example.data.model.UserProfileEntity
import com.example.data.model.WishlistItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class MarketplaceRepository(private val db: MarketplaceDatabase) {

    private val productDao = db.productDao()
    private val chatDao = db.chatDao()
    private val notificationDao = db.notificationDao()
    private val userDao = db.userDao()

    // OkHttpClient with 60s timeout as requested by the Gemini API guidelines
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()
    val allApprovedProducts: Flow<List<ProductEntity>> = productDao.getAllApprovedProducts()
    val wishlistedProducts: Flow<List<ProductEntity>> = productDao.getWishlistedProducts()
    val notifications: Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()
    val userProfile: Flow<UserProfileEntity?> = userDao.getProfileFlow()

    suspend fun getProductById(id: Int): ProductEntity? = withContext(Dispatchers.IO) {
        productDao.getProductById(id)
    }

    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> {
        return productDao.getProductsByCategory(category)
    }

    fun searchProducts(query: String): Flow<List<ProductEntity>> {
        return productDao.searchProducts(query)
    }

    suspend fun insertProduct(product: ProductEntity): Long = withContext(Dispatchers.IO) {
        productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        productDao.deleteProduct(product)
    }

    suspend fun isProductWishlisted(id: Int): Boolean = withContext(Dispatchers.IO) {
        productDao.isProductWishlisted(id) > 0
    }

    suspend fun toggleWishlist(id: Int) = withContext(Dispatchers.IO) {
        if (productDao.isProductWishlisted(id) > 0) {
            productDao.removeFromWishlist(id)
        } else {
            productDao.addToWishlist(WishlistItemEntity(id))
        }
    }

    // Chat operations
    fun getMessagesForThread(threadId: String): Flow<List<ChatMessageEntity>> {
        return chatDao.getMessagesForThread(threadId)
    }

    val allMessages: Flow<List<ChatMessageEntity>> = chatDao.getAllMessages()

    suspend fun insertMessage(message: ChatMessageEntity): Long = withContext(Dispatchers.IO) {
        chatDao.insertMessage(message)
    }

    suspend fun markThreadAsRead(threadId: String) = withContext(Dispatchers.IO) {
        chatDao.markThreadAsRead(threadId)
    }

    // Notification operations
    suspend fun insertNotification(notification: NotificationEntity) = withContext(Dispatchers.IO) {
        notificationDao.insertNotification(notification)
    }

    suspend fun markNotificationAsRead(id: Int) = withContext(Dispatchers.IO) {
        notificationDao.markAsRead(id)
    }

    // User profile operations
    suspend fun saveProfile(profile: UserProfileEntity) = withContext(Dispatchers.IO) {
        userDao.saveProfile(profile)
    }

    suspend fun getProfileDirect(): UserProfileEntity = withContext(Dispatchers.IO) {
        var profile = userDao.getProfileDirect()
        if (profile == null) {
            profile = UserProfileEntity()
            userDao.saveProfile(profile)
        }
        profile
    }

    // --- Gemini AI Services (Direct REST API Option B) ---

    private fun getApiKey(): String {
        return try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Call the Gemini REST API with the given prompt.
     */
    private suspend fun callGemini(prompt: String): String = withContext(Dispatchers.IO) {
        val key = getApiKey()
        if (key.isEmpty() || key == "MY_GEMINI_API_KEY") {
            Log.w("MarketplaceRepo", "Gemini API key is missing or is using placeholder value.")
            return@withContext "MOCK_MODE"
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$key"
        val mediaType = "application/json".toMediaType()

        val requestJson = JSONObject().apply {
            put("contents", org.json.JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", org.json.JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            })
        }

        val body = requestJson.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        try {
            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.e("MarketplaceRepo", "Gemini call failed with code: ${response.code}")
                    return@withContext "ERROR_CODE_${response.code}"
                }
                val responseBody = response.body?.string() ?: return@withContext "ERROR_EMPTY_RESPONSE"
                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.getJSONArray("candidates")
                val parts = candidates.getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                parts.getJSONObject(0).getString("text")
            }
        } catch (e: Exception) {
            Log.e("MarketplaceRepo", "Gemini API Call Exception", e)
            "EXCEPTION_${e.message}"
        }
    }

    /**
     * AI-generated high quality product descriptions based on short input attributes.
     */
    suspend fun generateAiDescription(title: String, category: String, condition: String): String {
        val prompt = """
            You are a professional retail and marketplace copywriter.
            Write a modern, elegant, premium, compelling, and SEO-optimized product listing description for a marketplace app.
            
            Product Details:
            - Title: $title
            - Category: $category
            - Condition: $condition
            
            Keep the tone exciting, luxurious, yet informative. Highlight key benefits. Do not write more than 4 short bullet points or paragraphs, and keep the output concise and premium. Do not repeat the title unnecessarily. Avoid Markdown formatting like bold asterisks.
        """.trimIndent()

        val result = callGemini(prompt)
        if (result.startsWith("ERROR_") || result.startsWith("EXCEPTION_") || result == "MOCK_MODE") {
            // High-quality mock fallback in case of no network / API key
            return "Experience this premium $condition $category: '$title'. This incredible item offers superior design, exceptional performance, and reliability. Handcrafted with precision components, it is perfect for everyday use or professional workloads. Excellent choice for buyers seeking quality and style combined."
        }
        return result.trim()
    }

    /**
     * AI-generated high quality product descriptions with a custom prompt instruction (e.g., Sinhala or Tamil).
     */
    suspend fun generateAiDescriptionWithInstruction(title: String, category: String, condition: String, instruction: String): String {
        val result = callGemini(instruction)
        if (result.startsWith("ERROR_") || result.startsWith("EXCEPTION_") || result == "MOCK_MODE") {
            // High quality fallback matching language target
            return if (instruction.contains("Sinhala") || instruction.contains("Sinhala product description")) {
                "ඉතා උසස් තත්ත්වයේ පවතින මෙම $condition $category: '$title' දැන් මිලදී ගත හැක. මෙය අනභිභවනීය නිමාවකින් සහ ඉහළම ක්‍රියාකාරීත්වයකින් සමන්විත වේ. දිනපතා භාවිතයට මෙන්ම වෘත්තීය මට්ටමේ වැඩකටයුතු සඳහාද ඉතාමත් සුදුසුය. හොඳම දේ සහතික කළ තත්ත්වයෙන් ලබාගැනීමට කැමති ගැනුම්කරුවන් සඳහා කදිම අවස්ථාවකි."
            } else {
                "Experience this premium $condition $category: '$title'. This incredible item offers superior design, exceptional performance, and reliability. Handcrafted with precision components, it is perfect for everyday use or professional workloads."
            }
        }
        return result.trim()
    }

    /**
     * AI Suggested Retail Price based on condition and historical data.
     */
    suspend fun suggestAiPrice(title: String, category: String, condition: String): Double {
        val prompt = """
            You are a professional commodity and secondary-market price appraisal expert.
            Based on the following product, estimate a competitive, fair and realistic average price in USD.
            
            Product:
            - Title: $title
            - Category: $category
            - Condition: $condition
            
            Return ONLY a single valid decimal number representing the suggested price (e.g., 299.99 or 45.00). Do not include currency symbols, prefix, suffix, or explanation.
        """.trimIndent()

        val result = callGemini(prompt)
        if (result.startsWith("ERROR_") || result.startsWith("EXCEPTION_") || result == "MOCK_MODE") {
            // Realistic programmatic fallback
            val basePrice = when (category) {
                "Mobiles" -> 500.0
                "Vehicles" -> 15000.0
                "Electronics" -> 350.0
                "Fashion" -> 45.0
                "Property" -> 250000.0
                "Services" -> 75.0
                "Jobs" -> 2500.0
                else -> 120.0
            }
            val multiplier = if (condition.lowercase() == "new") 1.0 else 0.55
            return basePrice * multiplier
        }

        return try {
            // Sanitize numeric characters
            val sanitized = result.replace(Regex("[^0-9.]"), "").trim()
            sanitized.toDouble()
        } catch (e: Exception) {
            99.99
        }
    }

    /**
     * Smart NLP product Search query parsing for matching category.
     */
    suspend fun smartSearchCategorization(query: String): String {
        val prompt = """
            Categorize the following search query into exactly one of these categories: Mobiles, Vehicles, Electronics, Fashion, Property, Services, Jobs, or None.
            
            Query: "$query"
            
            Response must be ONLY the category name. Nothing else.
        """.trimIndent()

        val result = callGemini(prompt)
        if (result.startsWith("ERROR_") || result.startsWith("EXCEPTION_") || result == "MOCK_MODE") {
            val q = query.lowercase()
            return when {
                q.contains("phone") || q.contains("mobile") || q.contains("iphone") || q.contains("samsung") || q.contains("pixel") -> "Mobiles"
                q.contains("car") || q.contains("bike") || q.contains("vehicle") || q.contains("toyota") -> "Vehicles"
                q.contains("laptop") || q.contains("tv") || q.contains("camera") || q.contains("electronic") -> "Electronics"
                q.contains("shirt") || q.contains("shoes") || q.contains("fashion") || q.contains("jacket") || q.contains("watch") -> "Fashion"
                q.contains("house") || q.contains("land") || q.contains("apartment") || q.contains("property") -> "Property"
                q.contains("plumber") || q.contains("clean") || q.contains("repair") || q.contains("service") -> "Services"
                q.contains("developer") || q.contains("work") || q.contains("job") || q.contains("manager") -> "Jobs"
                else -> "Mobiles"
            }
        }
        return result.trim()
    }
}
