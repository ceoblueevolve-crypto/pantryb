package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory_items")
data class InventoryItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String, // "Produce", "Dairy & Eggs", "Pantry", "Meat & Seafood", "Beverages", "Snacks", "Frozen", "Household"
    val location: String, // "Fridge", "Pantry", "Freezer", "Spice Rack"
    val quantity: Double,
    val unit: String, // "units", "liters", "kg", "packs", "bottles", "boxes"
    val minThreshold: Double,
    val expirationDateEpoch: Long,
    val barcode: String = "",
    val lastRestockedEpoch: Long = System.currentTimeMillis(),
    val dailyConsumptionRate: Double = 0.2, // items/units consumed per day (used by Oracle 26AI)
    val cloudSyncStatus: String = "SYNCED", // "SYNCED", "PENDING_PUSH", "LOCAL_CACHED"
    val encryptedPayloadHash: String = ""
)

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val inventoryItemId: Long? = null,
    val name: String,
    val category: String,
    val quantity: Double,
    val unit: String,
    val isPurchased: Boolean = false,
    val addedByFamilyMember: String = "Mom", // "Mom", "Dad", "Maya", "Alex", "Oracle 26AI Auto-Restock"
    val purchasedByFamilyMember: String? = null,
    val priority: String = "NORMAL", // "HIGH", "NORMAL", "LOW"
    val estimatedPrice: Double = 3.99,
    val createdEpoch: Long = System.currentTimeMillis(),
    val cloudSyncStatus: String = "SYNCED"
)

@Entity(tableName = "restock_alerts")
data class RestockAlertEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val inventoryItemId: Long,
    val itemName: String,
    val category: String,
    val currentQuantity: Double,
    val thresholdQuantity: Double,
    val unit: String,
    val urgency: String, // "CRITICAL_OUT_OF_STOCK", "LOW_STOCK", "EXPIRING_SOON"
    val alertMessage: String,
    val isDismissed: Boolean = false,
    val createdEpoch: Long = System.currentTimeMillis()
)

@Entity(tableName = "oracle_sync_logs")
data class OracleSyncLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestampEpoch: Long = System.currentTimeMillis(),
    val action: String, // "PUSH", "PULL", "ORACLE_AI_INFERENCE", "E2EE_TRANSACTION"
    val recordsCount: Int,
    val status: String, // "SUCCESS", "PENDING", "OFFLINE_CACHED"
    val latencyMs: Long,
    val oracleVectorEmbeddingSignature: String = "vec26ai_${System.currentTimeMillis() % 10000}"
)

@Entity(tableName = "product_categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconEmoji: String = "📦",
    val description: String = "",
    val colorHex: String = "#10B981",
    val defaultLocation: String = "Pantry",
    val createdEpoch: Long = System.currentTimeMillis()
)

@Entity(tableName = "storage_locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconEmoji: String = "🏠",
    val description: String = "",
    val colorHex: String = "#3B82F6",
    val temperatureZone: String = "Ambient",
    val createdEpoch: Long = System.currentTimeMillis()
)

@Entity(tableName = "family_members")
data class FamilyMemberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val role: String = "Family Member",
    val avatarEmoji: String = "👤",
    val colorHex: String = "#3B82F6",
    val dietaryNotes: String = "",
    val preferredLanguage: String = "en",
    val isDefault: Boolean = false,
    val createdEpoch: Long = System.currentTimeMillis()
)

@Entity(tableName = "temperature_zones")
data class TemperatureZoneEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconEmoji: String = "🌡️",
    val description: String = "",
    val targetRange: String = "Ambient",
    val colorHex: String = "#3B82F6",
    val createdEpoch: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_preferences")
data class UserPreferenceEntity(
    @PrimaryKey
    val profileName: String = "Default",
    val selectedLanguage: String = "en",
    val autoDetectLanguage: Boolean = true,
    val measurementSystem: String = "Metric (kg, g, L)",
    val currencySymbol: String = "$",
    val enableAiTranslation: Boolean = true,
    val updatedEpoch: Long = System.currentTimeMillis()
)



