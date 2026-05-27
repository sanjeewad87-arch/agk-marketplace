package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.local.MarketplaceDatabase
import com.example.data.model.ChatMessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ProductEntity
import com.example.data.model.UserProfileEntity
import com.example.data.repository.MarketplaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MarketplaceViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        MarketplaceDatabase::class.java, "agk_marketplace.db"
    ).fallbackToDestructiveMigration().build()

    val repository = MarketplaceRepository(db)

    // Language Localizations
    // Multi-Language Strings Map: [Lang] -> [Key] -> [TextValue]
    private val translations = mapOf(
        "EN" to mapOf(
            "app_title" to "AGK Marketplace",
            "search_hint" to "Search products, cars, jobs...",
            "categories" to "Categories",
            "trending" to "Trending Hotspots",
            "recommended" to "Recommended For You",
            "nearby" to "Nearby Sellers (GPS)",
            "all_regions" to "All Sri Lanka",
            "mobiles" to "Mobiles",
            "vehicles" to "Vehicles",
            "electronics" to "Electronics",
            "fashion" to "Fashion",
            "property" to "Property",
            "services" to "Services",
            "jobs" to "Jobs",
            "condition_new" to "Brand New",
            "condition_used" to "Used / Like New",
            "price_suggest" to "AI Price Estimate",
            "gen_desc_btn" to "AI Autocompleter",
            "chats" to "Secure Inbox",
            "chat_seen" to "Read",
            "chat_unseen" to "Delivered",
            "my_listings" to "My Listings",
            "admin_panel" to "Admin Board",
            "checkout_headline" to "Choose Gateway",
            "boost_ad" to "Promote Ad (Premium)",
            "voice_search_speak" to "Speak now... Gemini AI is listening",
            "safety_warning_title" to "SAFETY WARNING",
            "safety_warning_desc" to "Do NOT make payments without inspecting the item beforehand.",
            "safety_item_1" to "• Beware of scams & mock listings",
            "safety_item_2" to "• Meet sellers in safe public places (e.g., Galle Face, shopping malls)",
            "safety_item_3" to "• Always verify products physically before transferring money",
            "seller_dashboard" to "Seller Dashboard",
            "buyer_dashboard" to "Buyer Dashboard",
            "manage_listings" to "Manage Listings",
            "manage_orders" to "Manage Orders",
            "sold_history" to "Sold History",
            "saved_searches" to "Saved Searches",
            "order_history" to "My Purchase Ledger",
            "recently_viewed" to "Recently Viewed Ads",
            "saved_search_status" to "Search keyword saved!",
            "make_offer" to "Price Negotiation (Make an Offer)",
            "make_offer_placeholder" to "Enter offer price in LKR/USD",
            "similar_ads" to "Similar Ads Around You",
            "seller_rating_text" to "Seller Rating Index",
            "seller_verified" to "Verified Merchant Badge"
        ),
        "SI" to mapOf(
            "app_title" to "AGK වෙළඳපොළ",
            "search_hint" to "භාණ්ඩ, වාහන, රැකියා සොයන්න...",
            "categories" to "ප්‍රවර්ග",
            "trending" to "ජනප්‍රිය වෙළඳ දැන්වීම්",
            "recommended" to "ඔබට ගැලපෙන භාණ්ඩ",
            "nearby" to "අසල පිහිටි විකුණුම්කරුවන්",
            "all_regions" to "මුළු ශ්‍රී ලංකාවම",
            "mobiles" to "ජංගම දුරකථන",
            "vehicles" to "වාහන",
            "electronics" to "විද්‍යුත් උපකරණ",
            "fashion" to "විලාසිතා",
            "property" to "ඉඩකඩම්",
            "services" to "සේවාවන්",
            "jobs" to "රැකියා",
            "condition_new" to "අලුත්ම",
            "condition_used" to "භාවිතා කල",
            "price_suggest" to "AI මිල නිමානය",
            "gen_desc_btn" to "AI විස්තරය",
            "chats" to "පණිවිඩ පෙට්ටිය",
            "chat_seen" to "කියවා ඇත",
            "chat_unseen" to "ලැබුණි",
            "my_listings" to "මගේ දැන්වීම්",
            "admin_panel" to "පාලක මණ්ඩලය",
            "checkout_headline" to "ගෙවීම් ක්‍රමය",
            "boost_ad" to "දැන්වීම් ප්‍රවර්ධනය කරන්න",
            "voice_search_speak" to "දැන් කතා කරන්න... AI සවන් දෙයි",
            "safety_warning_title" to "ආරක්‍ෂිත අනතුරු ඇඟවීම",
            "safety_warning_desc" to "භාණ්ඩ පරීක්ෂා කිරීමකින් තොරව මුදල් ගෙවීම් නොකරන්න.",
            "safety_item_1" to "• වංචනිකයන්ගෙන් ප්‍රවේශම් වන්න",
            "safety_item_2" to "• ආරක්ෂිත පොදු ස්ථානවලදී පමණක් විකුණුම්කරුවන් හමුවන්න",
            "safety_item_3" to "• ගෙවීමට පෙර භාණ්ඩයේ නිවැරදිභාවය තහවුරු කරගන්න",
            "seller_dashboard" to "විකුණුම්කරුගේ උපකරණ පුවරුව",
            "buyer_dashboard" to "මිලදී ගන්නාගේ උපකරණ පුවරුව",
            "manage_listings" to "දැන්වීම් කළමනාකරණය",
            "manage_orders" to "ඇණවුම් කළමනාකරණය",
            "sold_history" to "විකිණුම් ඉතිහාසය",
            "saved_searches" to "සුරැකි සෙවීම්",
            "order_history" to "ගනුදෙනු ඉතිහාසය (ප්‍රධාන ලෙජරය)",
            "recently_viewed" to "මෑතකදී නැරඹූ දැන්වීම්",
            "saved_search_status" to "සෙවුම් පදය සාර්ථකව සුරකින ලදී!",
            "make_offer" to "මිල සාකච්ඡා කිරීම (මිලක් යෝජනා කරන්න)",
            "make_offer_placeholder" to "ඔබේ මිල යෝජනාව ඇතුළත් කරන්න",
            "similar_ads" to "ඔබ අවට ඇති සමාන දැන්වීම්",
            "seller_rating_text" to "විකුණුම්කරුගේ තක්සේරු දර්ශකය",
            "seller_verified" to "තහවුරු කරන ලද වෙළඳ සංකේතය"
        ),
        "TA" to mapOf(
            "app_title" to "AGK சந்தை",
            "search_hint" to "மொபைல், கார்கள், வேலைகளைத் தேடுங்கள்...",
            "categories" to "வகைகள்",
            "trending" to "பிரபலமானவை",
            "recommended" to "உங்களுக்காக பரிந்துரைக்கப்பட்டவை",
            "nearby" to "அருகிலுள்ள விற்பனையாளர்கள்",
            "all_regions" to "இலங்கை முழுவதும்",
            "mobiles" to "மொபைல்கள்",
            "vehicles" to "வாகனங்கள்",
            "electronics" to "மின்னணுவியல்",
            "fashion" to "ஆடைகள்",
            "property" to "சொத்துக்கள்",
            "services" to "சேவைகள்",
            "jobs" to "வேலைகள்",
            "condition_new" to "புதியது",
            "condition_used" to "பயன்படுத்தப்பட்டது",
            "price_suggest" to "AI விலை மதிப்பீடு",
            "gen_desc_btn" to "AI குறியீடு",
            "chats" to "செய்திகள்",
            "chat_seen" to "பார்த்துவிட்டேன்",
            "chat_unseen" to "அனுப்பப்பட்டது",
            "my_listings" to "எனது விளம்பரங்கள்",
            "admin_panel" to "நிர்வாக குழு",
            "checkout_headline" to "பணம் செலுத்தும் முறை",
            "boost_ad" to "விளம்பரம் செய்யவும்",
            "voice_search_speak" to "இப்போது பேசுங்கள்... AI கேட்கிறது",
            "safety_warning_title" to "பாதுகாப்பு எச்சரிக்கை",
            "safety_warning_desc" to "பொருட்களை பரிசோதிக்காமல் பணம் செலுத்த வேண்டாம்.",
            "safety_item_1" to "• ஏமாற்றுக்காரர்களிடம் கவனமாக இருங்கள்",
            "safety_item_2" to "• பாதுகாப்பான பொது இடங்களில் விற்பனையாளர்களை சந்திக்கவும்",
            "safety_item_3" to "• பணம் செலுத்துவதற்கு முன் தயாரிப்புகளை சரிபார்க்கவும்",
            "seller_dashboard" to "விற்பனையாளர் டாஷ்போர்டு",
            "buyer_dashboard" to "வாங்குபவர் டாஷ்போர்டு",
            "manage_listings" to "விளம்பரங்களை நிர்வகிக்கவும்",
            "manage_orders" to "ஆர்டர்களை நிர்வகிக்கவும்",
            "sold_history" to "விற்பனை வரலாறு",
            "saved_searches" to "சேமிக்கப்பட்ட தேடல்கள்",
            "order_history" to "கொள்முதல் வரலாறு",
            "recently_viewed" to "சமீபத்தில் பார்த்தவை",
            "saved_search_status" to "தேடல் சேமிக்கப்பட்டது!",
            "make_offer" to "விலை பேரம் பேசுதல்",
            "make_offer_placeholder" to "விலையை உள்ளிடவும்",
            "similar_ads" to "உங்களை சுற்றியுள்ள ஒத்த விளம்பரங்கள்",
            "seller_rating_text" to "விற்பனையாளர் மதிப்பீடு",
            "seller_verified" to "சரிபார்க்கப்பட்ட விற்பனையாளர்"
        )
    )

    // Database flows
    val allProducts: StateFlow<List<ProductEntity>> = repository.allApprovedProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adminAllProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistedProducts: StateFlow<List<ProductEntity>> = repository.wishlistedProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val notifications: StateFlow<List<NotificationEntity>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMessages: StateFlow<List<ChatMessageEntity>> = repository.allMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dashboards flows (Buyer & Seller)
    val allOrders = MutableStateFlow<List<OrderEntity>>(listOf(
        OrderEntity(
            id = "TX-849202",
            productTitle = "Sony WH-1000XM5 Active ANC",
            price = 289.00,
            paymentMethod = "STRIPE",
            status = "COMPLETED",
            sellerName = "Admin Merchant",
            buyerName = "Sanjeewa D.",
            timestamp = System.currentTimeMillis() - 86400000 * 2
        ),
        OrderEntity(
            id = "TX-105820",
            productTitle = "Toyota Land Cruiser Prado TX L",
            price = 37500.00,
            paymentMethod = "PAYHERE",
            status = "PENDING",
            sellerName = "Kamal K.",
            buyerName = "Sanjeewa D.",
            timestamp = System.currentTimeMillis() - 360000 * 10
        )
    ))

    val savedSearches = MutableStateFlow<List<String>>(listOf("iPhone 15", "Toyota Prado", "Audio System"))
    val recentlyViewed = MutableStateFlow<List<ProductEntity>>(emptyList())

    // UI States
    val activeLanguage = MutableStateFlow("EN")
    val selectedCategory = MutableStateFlow("All")
    val searchQuery = MutableStateFlow("")
    val activeScreen = MutableStateFlow("home") // home, search, add, chat, profile, admin, checkout, details
    val activeProductDetailId = MutableStateFlow<Int?>(null)
    val activeChatThreadId = MutableStateFlow<String?>(null)
    val activeChatRecipient = MutableStateFlow("Seller")

    // Nearby Filter state (GPS)
    val filterNearbyOnly = MutableStateFlow(false)

    // Current logged-in state of simulated auth
    val isLoggedIn = MutableStateFlow(true) // Automatically logged in with credentials in workspace: SanjeewaD87@gmail.com
    val userEmail = "SanjeewaD87@gmail.com"

    // AI dynamic progress fields
    private val _isGeneratingDesc = MutableStateFlow(false)
    val isGeneratingDesc = _isGeneratingDesc.asStateFlow()

    private val _isGeneratingPrice = MutableStateFlow(false)
    val isGeneratingPrice = _isGeneratingPrice.asStateFlow()

    private val _isSmartSearching = MutableStateFlow(false)
    val isSmartSearching = _isSmartSearching.asStateFlow()

    init {
        // Auto-populate custom database on launch if empty
        viewModelScope.launch {
            repository.allProducts.firstOrNull()?.let { list ->
                if (list.isEmpty()) {
                    populateInitialData()
                }
            }
            // Auto initialize user profile
            repository.getProfileDirect()
        }
    }

    // Translation utilities
    fun translate(key: String): String {
        val lang = activeLanguage.value
        return translations[lang]?.get(key) ?: translations["EN"]?.get(key) ?: key
    }

    fun formatPrice(price: Double): String {
        val lang = activeLanguage.value
        val formatted = java.text.DecimalFormat("#,##0").format(price)
        val symbol = if (lang == "SI") "රු." else "Rs."
        return "$symbol $formatted"
    }

    // Auth flows
    fun logout() {
        isLoggedIn.value = false
        activeScreen.value = "auth"
    }

    fun login(email: String) {
        viewModelScope.launch {
            isLoggedIn.value = true
            val profile = repository.getProfileDirect()
            repository.saveProfile(profile.copy(email = email, name = email.substringBefore("@")))
            activeScreen.value = "home"
        }
    }

    fun saveProfile(profile: UserProfileEntity) {
        viewModelScope.launch {
            repository.saveProfile(profile)
        }
    }

    // Wishlist Toggle
    fun toggleProductWishlist(id: Int) {
        viewModelScope.launch {
            repository.toggleWishlist(id)
        }
    }

    // Chat mechanics
    fun selectChatThread(threadId: String, recipientName: String) {
        activeChatThreadId.value = threadId
        activeChatRecipient.value = recipientName
        activeScreen.value = "chat_detail"
        viewModelScope.launch {
            repository.markThreadAsRead(threadId)
        }
    }

    fun sendChatMessage(text: String, mediaType: String = "TEXT", mediaUrl: String? = null) {
        val threadId = activeChatThreadId.value ?: return
        val otherUser = activeChatRecipient.value
        val lang = activeLanguage.value

        viewModelScope.launch {
            // Write sender message
            val senderMsg = ChatMessageEntity(
                threadId = threadId,
                senderId = "me",
                receiverId = otherUser,
                textContent = text,
                messageType = mediaType,
                mediaUrl = mediaUrl
            )
            repository.insertMessage(senderMsg)

            // Dynamic automated receiver mock response with a short delay
            kotlinx.coroutines.delay(1200)

            val replyText = if (lang == "SI") {
                when {
                    mediaType == "VOICE" -> "හරි, මට ඔබේ හඬ පණිවිඩය ලැබුණා. අපි ගාල්ල කොටුව ආසන්නයේ හමුවීමට වේලාවක් සකස් කරමු."
                    mediaType == "IMAGE" -> "නියමයි, භාණ්ඩය ඉතා හොඳ තත්ත්වයේ පවතින බව පෙනේ. මිල අඩු කළ හැකිද?"
                    text.lowercase().contains("price") || text.contains("මිල") -> "ඔබ අද මිලදී ගන්නේ නම් මට 5% ක විශේෂ වට්ටමක් ලබා දිය හැකිය!"
                    text.lowercase().contains("location") || text.contains("ස්ථානය") -> "මම කොළඹ 03 පිහිටා ඇත, ලිබර්ටි ප්ලාසා ආසන්නයේ."
                    else -> "නියමයි! අපට භාණ්ඩ ලැබුණු පසු ගෙවීමේ ක්‍රමය (COD) හෝ ස්ට්‍රයිප් බැංකු ගෙවීමක් සකසා ගත හැක."
                }
            } else if (lang == "TA") {
                when {
                    mediaType == "VOICE" -> "சரி, உங்கள் குரல் செய்தி எனக்குக் கிடைத்தது. காலி கோட்டைக்கு அருகில் சந்திக்க நேரம் ஒதுக்குவோம்."
                    mediaType == "IMAGE" -> "அற்புதம், பொருள் நல்ல நிலையில் உள்ளது போல தெரிகிறது. விலை குறைக்க முடியுமா?"
                    text.lowercase().contains("price") -> "நீங்கள் இன்று வாங்கினால் 5% தள்ளுபடி வழங்க முடியும்!"
                    text.lowercase().contains("location") -> "நான் கொழும்பு 03ல் லிபர்ட்டி பிளாசா அருகில் இருக்கிறேன்."
                    else -> "மிக்க நன்று! பொருட்கள் கிடைத்ததும் பணம் செலுத்தலாம் அல்லது ஸ்ட்ரைப் மூலம் பணம் செலுத்தலாம்."
                }
            } else {
                when {
                    mediaType == "VOICE" -> "Okay, I got your voice message. Let's arrange a time to meet up near Galle Fort."
                    mediaType == "IMAGE" -> "Wow, the item looks in pristine state. Is the price negotiable?"
                    text.lowercase().contains("price") -> "I can offer a 5% discount if you purchase it today!"
                    text.lowercase().contains("location") -> "I am located in Colombo 03, near the Liberty Plaza."
                    else -> "Great! Let's arrange cash on delivery or Stripe bank payment in the checkout panel."
                }
            }

            val receiverMsg = ChatMessageEntity(
                threadId = threadId,
                senderId = otherUser,
                receiverId = "me",
                textContent = replyText,
                messageType = "TEXT"
            )
            repository.insertMessage(receiverMsg)

            // Trigger notification
            repository.insertNotification(
                NotificationEntity(
                    title = if (lang == "SI") "නව පණිවිඩයක්: $otherUser" else if (lang == "TA") "புதிய செய்தி: $otherUser" else "New Message from $otherUser",
                    content = replyText,
                    type = "CHAT"
                )
            )
        }
    }

    // AI logic powered by Gemini & Fallback
    fun generateAiDescriptionForListing(title: String, category: String, condition: String, onCompleted: (String) -> Unit) {
        val lang = activeLanguage.value
        viewModelScope.launch {
            _isGeneratingDesc.value = true
            // If the language is Sinhala, adjust the prompt instruction to return native descriptions
            val instruction = if (lang == "SI") {
                "Generate a professional, inviting Sinhala marketplace product description for a $condition $category ad titled '$title'. Return only the description text in local Sinhala."
            } else if (lang == "TA") {
                "Generate a professional marketplace description in Tamil for a $condition $category ad titled '$title'."
            } else {
                "Generate a professional, inviting product description for a $condition $category ad titled '$title'."
            }
            val desc = repository.generateAiDescriptionWithInstruction(title, category, condition, instruction)
            _isGeneratingDesc.value = false
            onCompleted(desc)
        }
    }

    fun generateAiPriceEstimate(title: String, category: String, condition: String, onCompleted: (Double) -> Unit) {
        viewModelScope.launch {
            _isGeneratingPrice.value = true
            val price = repository.suggestAiPrice(title, category, condition)
            _isGeneratingPrice.value = false
            onCompleted(price)
        }
    }

    fun triggerSmartSearch(query: String) {
        viewModelScope.launch {
            _isSmartSearching.value = true
            val category = repository.smartSearchCategorization(query)
            _isSmartSearching.value = false
            selectedCategory.value = category
            searchQuery.value = query
        }
    }

    // Add Listing logic
    fun createListing(
        title: String,
        description: String,
        price: Double,
        category: String,
        condition: String,
        location: String,
        imageUrls: String
    ) {
        val lang = activeLanguage.value
        viewModelScope.launch {
            val user = repository.getProfileDirect()
            val product = ProductEntity(
                title = title,
                description = description,
                price = price,
                category = category,
                condition = condition,
                location = location,
                imageUrls = imageUrls,
                sellerName = user.name,
                sellerPhone = user.phone,
                views = 1,
                isApproved = false // Go to approval system queue
            )
            repository.insertProduct(product)

            repository.insertNotification(
                NotificationEntity(
                    title = if (lang == "SI") "දැන්වීම අනුමැතිය සඳහා යොමු කරන ලදී" else "Listing Sent for Verification",
                    content = if (lang == "SI") "ඔබගේ '$title' දැන්වීම පාලක මණ්ඩලයේ ආරක්‍ෂිත පරීක්ෂාවෙන් පසු සක්‍රීය වනු ඇත." else "Your product '$title' is awaiting automated safety approval checks.",
                    type = "APPROVAL"
                )
            )
        }
    }

    // Admin board controls
    fun toggleProductApproval(id: Int) {
        viewModelScope.launch {
            repository.getProductById(id)?.let { product ->
                val updated = product.copy(isApproved = !product.isApproved)
                repository.updateProduct(updated)
            }
        }
    }

    fun removeProductListing(id: Int) {
        viewModelScope.launch {
            repository.getProductById(id)?.let { product ->
                repository.deleteProduct(product)
            }
        }
    }

    fun boostProductAd(id: Int) {
        viewModelScope.launch {
            repository.getProductById(id)?.let { product ->
                val updated = product.copy(isFeatured = true)
                repository.updateProduct(updated)
                repository.insertNotification(
                    NotificationEntity(
                        title = "Product Boost Live!",
                        content = "'${product.title}' has been boosted to recommended hotspots.",
                        type = "PROMOTION"
                    )
                )
            }
        }
    }

    // Populate Initial Mock Marketplace listing
    private suspend fun populateInitialData() {
        val lists = listOf(
            ProductEntity(
                title = "iPhone 15 Pro Max - Blue Titanium",
                description = "256GB storage, Singapore stock. Includes full pristine box, premium ringke case, and unused charging cable. Battery health is at excellent 98%. Under Apple Sri Lanka care warranty.",
                price = 949.00,
                category = "Mobiles",
                condition = "Used",
                location = "Colombo 04",
                imageUrls = "mobiles_iphone",
                isApproved = true,
                isFeatured = true,
                views = 1530
            ),
            ProductEntity(
                title = "Toyota Land Cruiser Prado TX L",
                description = "Super luxury SUV. Model year 2021, registered in 2022. First owner, mint interior. Dual panoramic roof, beige leather seats, 360 surround-view visual display.",
                price = 37500.00,
                category = "Vehicles",
                condition = "Used",
                location = "Galle Fort",
                imageUrls = "vehicles_prado",
                isApproved = true,
                isFeatured = true,
                views = 3590
            ),
            ProductEntity(
                title = "Sony WH-1000XM5 Active ANC",
                description = "Brand new sealed in pristine retail box. Custom Active Sound cancelling headphones with industry-ranking acoustic design, 30 hours battery backup.",
                price = 289.00,
                category = "Electronics",
                condition = "New",
                location = "Colombo 03",
                imageUrls = "electronics_sony",
                isApproved = true,
                isFeatured = false,
                views = 142
            ),
            ProductEntity(
                title = "Limited Futurist Cyber Jacket",
                description = "Styled with premium glassmorphic visual overlays, waterproof neo-membrane weave. Excellent apparel match for cyberpunk enthusiasts. Unisex collection.",
                price = 145.00,
                category = "Fashion",
                condition = "New",
                location = "Kandy Town",
                imageUrls = "fashion_jacket",
                isApproved = true,
                isFeatured = true,
                views = 528
            ),
            ProductEntity(
                title = "Luxury Penthouse Suite - Havelock",
                description = "3 state-of-the-art bedroom penthouse spanning 2400 sqft. Equipped with a rooftop pool, glass balcony offering breathtaking ocean sunsets, dedicated automated garage.",
                price = 145000.00,
                category = "Property",
                condition = "New",
                location = "Colombo 06",
                imageUrls = "property_penthouse",
                isApproved = true,
                isFeatured = false,
                views = 812
            ),
            ProductEntity(
                title = "Senior Mobile App Developer (Flutter & Android)",
                description = "Looking for an expert application architect to develop premium native cross-platform services. Contract-based remote role paid in USD.",
                price = 5000.00,
                category = "Jobs",
                condition = "New",
                location = "Colombo 01",
                imageUrls = "jobs_developer",
                isApproved = true,
                isFeatured = false,
                views = 301
            )
        )

        lists.forEach {
            repository.insertProduct(it)
        }

        // Mock generic inbox messages so the chats look active
        repository.insertMessage(
            ChatMessageEntity(
                threadId = "buyer_sanjeewa",
                senderId = "Sanjeewa D.",
                receiverId = "me",
                textContent = "Hello! Is the Land Cruiser Prado TX still available to inspect this weekend?"
            )
        )

        repository.insertMessage(
            ChatMessageEntity(
                threadId = "buyer_kamal",
                senderId = "Kamal K.",
                receiverId = "me",
                textContent = "I'm interested in the WH-1000XM5. Where can we meet?"
            )
        )

        // Mock Initial Notifications
        repository.insertNotification(
            NotificationEntity(
                title = "Welcome to AGK Marketplace",
                content = "Browse state-of-the-art listings, boost your ads, or chat with buyers using premium AI description generators.",
                type = "PROMOTION"
            )
        )
    }

    // Buyer-Seller operations
    fun createOrder(productTitle: String, price: Double, paymentMethod: String, sellerName: String) {
        val newOrder = OrderEntity(
            id = "TX-" + System.currentTimeMillis().toString().takeLast(6),
            productTitle = productTitle,
            price = price,
            paymentMethod = paymentMethod,
            status = "PENDING",
            sellerName = sellerName,
            buyerName = userProfile.value?.name ?: "Sanjeewa D.",
            timestamp = System.currentTimeMillis()
        )
        allOrders.value = listOf(newOrder) + allOrders.value
    }

    fun saveSearchQuery(query: String) {
        if (query.isNotBlank() && !savedSearches.value.contains(query)) {
            savedSearches.value = listOf(query) + savedSearches.value
        }
    }

    fun deleteSavedSearch(query: String) {
        savedSearches.value = savedSearches.value.filter { it != query }
    }

    fun addToRecentlyViewed(product: ProductEntity) {
        if (!recentlyViewed.value.any { it.id == product.id }) {
            recentlyViewed.value = (listOf(product) + recentlyViewed.value).take(5)
        }
    }
}

data class OrderEntity(
    val id: String,
    val productTitle: String,
    val price: Double,
    val paymentMethod: String,
    val status: String,
    val sellerName: String,
    val buyerName: String,
    val timestamp: Long
)
