package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.FamilyMemberEntity
import com.example.data.local.entity.InventoryItemEntity
import com.example.data.local.entity.LocationEntity
import com.example.data.local.entity.OracleSyncLogEntity
import com.example.data.local.entity.RestockAlertEntity
import com.example.data.local.entity.ShoppingItemEntity
import com.example.data.local.entity.TemperatureZoneEntity
import com.example.data.local.entity.UserPreferenceEntity
import com.example.data.oracle.CloudConnectionState
import com.example.data.oracle.Oracle26AiService
import com.example.data.oracle.PredictiveAnalyticsEngine
import com.example.data.oracle.PredictiveAnalyticsReport
import com.example.data.oracle.SyncResult
import com.example.data.security.EncryptionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class PantryRepository(
    private val database: AppDatabase,
    val oracleService: Oracle26AiService
) {
    private val inventoryDao = database.inventoryDao()
    private val shoppingDao = database.shoppingDao()
    private val alertDao = database.alertDao()
    private val syncLogDao = database.syncLogDao()
    private val categoryDao = database.categoryDao()
    private val locationDao = database.locationDao()
    private val familyMemberDao = database.familyMemberDao()
    private val temperatureZoneDao = database.temperatureZoneDao()
    private val userPreferenceDao = database.userPreferenceDao()

    val allInventoryItems: Flow<List<InventoryItemEntity>> = inventoryDao.getAllInventoryItems()
    val lowStockItems: Flow<List<InventoryItemEntity>> = inventoryDao.getLowStockItems()
    val allShoppingItems: Flow<List<ShoppingItemEntity>> = shoppingDao.getAllShoppingItems()
    val activeAlerts: Flow<List<RestockAlertEntity>> = alertDao.getActiveAlerts()
    val syncLogs: Flow<List<OracleSyncLogEntity>> = syncLogDao.getRecentLogs()
    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()
    val allLocations: Flow<List<LocationEntity>> = locationDao.getAllLocations()
    val allFamilyMembers: Flow<List<FamilyMemberEntity>> = familyMemberDao.getAllFamilyMembers()
    val allTemperatureZones: Flow<List<TemperatureZoneEntity>> = temperatureZoneDao.getAllTemperatureZones()
    val userPreference: Flow<UserPreferenceEntity?> = userPreferenceDao.getDefaultPreference()

    suspend fun seedInitialDataIfEmpty() {
        // Seed initial temperature zones if none exist
        if (temperatureZoneDao.getZoneCount() == 0) {
            val defaultTempZones = listOf(
                TemperatureZoneEntity(name = "Deep Freeze", iconEmoji = "🧊", description = "Sub-zero preservation for meats, seafood, prepared dishes, and ice", colorHex = "#06B6D4", targetRange = "0°F / -18°C"),
                TemperatureZoneEntity(name = "Refrigerated", iconEmoji = "❄️", description = "Chilled preservation for dairy, produce, beverages, and fresh proteins", colorHex = "#3B82F6", targetRange = "37°F / 3°C"),
                TemperatureZoneEntity(name = "Cellar Cool", iconEmoji = "🍷", description = "Optimal humidity & cool dark storage for fine wines, root vegetables, and preserves", colorHex = "#8B5CF6", targetRange = "55°F / 13°C"),
                TemperatureZoneEntity(name = "Room Temp / Pantry", iconEmoji = "🚪", description = "Dry ambient shelf storage for grains, pasta, spices, and canned staples", colorHex = "#F59E0B", targetRange = "68°F / 20°C"),
                TemperatureZoneEntity(name = "Warm / Countertop", iconEmoji = "🍞", description = "Ambient room temperature for ripening fruit, fresh artisanal bakery, and daily snacks", colorHex = "#D97706", targetRange = "72°F / 22°C")
            )
            temperatureZoneDao.insertZones(defaultTempZones)
        }

        val currentItems = allInventoryItems.first()
        if (currentItems.isEmpty()) {
            val now = System.currentTimeMillis()
            val dayMs = 86400000L

            val initialInventory = listOf(
                InventoryItemEntity(
                    name = "Organic Whole Milk",
                    category = "Dairy & Eggs",
                    location = "Fridge",
                    quantity = 0.4,
                    unit = "liters",
                    minThreshold = 1.0,
                    expirationDateEpoch = now + (4 * dayMs),
                    barcode = "011110416001",
                    dailyConsumptionRate = 0.4,
                    cloudSyncStatus = "SYNCED",
                    encryptedPayloadHash = EncryptionManager.encrypt("Organic Whole Milk|0.4L")
                ),
                InventoryItemEntity(
                    name = "Pasture-Raised Eggs",
                    category = "Dairy & Eggs",
                    location = "Fridge",
                    quantity = 2.0,
                    unit = "units",
                    minThreshold = 6.0,
                    expirationDateEpoch = now + (12 * dayMs),
                    barcode = "085000041234",
                    dailyConsumptionRate = 1.0,
                    cloudSyncStatus = "SYNCED",
                    encryptedPayloadHash = EncryptionManager.encrypt("Pasture-Raised Eggs|2units")
                ),
                InventoryItemEntity(
                    name = "Extra Virgin Olive Oil",
                    category = "Pantry",
                    location = "Pantry",
                    quantity = 1.0,
                    unit = "bottles",
                    minThreshold = 0.5,
                    expirationDateEpoch = now + (180 * dayMs),
                    barcode = "041303001289",
                    dailyConsumptionRate = 0.05,
                    cloudSyncStatus = "SYNCED",
                    encryptedPayloadHash = EncryptionManager.encrypt("Extra Virgin Olive Oil|1bottle")
                ),
                InventoryItemEntity(
                    name = "Baby Spinach (Organic)",
                    category = "Produce",
                    location = "Fridge",
                    quantity = 0.5,
                    unit = "packs",
                    minThreshold = 1.0,
                    expirationDateEpoch = now + (2 * dayMs), // Expiring soon!
                    barcode = "071430009941",
                    dailyConsumptionRate = 0.3,
                    cloudSyncStatus = "SYNCED",
                    encryptedPayloadHash = EncryptionManager.encrypt("Baby Spinach|0.5pack")
                ),
                InventoryItemEntity(
                    name = "Greek Vanilla Yogurt",
                    category = "Dairy & Eggs",
                    location = "Fridge",
                    quantity = 0.3,
                    unit = "kg",
                    minThreshold = 0.6,
                    expirationDateEpoch = now + (6 * dayMs),
                    barcode = "052159000142",
                    dailyConsumptionRate = 0.15,
                    cloudSyncStatus = "SYNCED",
                    encryptedPayloadHash = EncryptionManager.encrypt("Greek Vanilla Yogurt|0.3kg")
                ),
                InventoryItemEntity(
                    name = "Hass Avocados",
                    category = "Produce",
                    location = "Pantry",
                    quantity = 3.0,
                    unit = "units",
                    minThreshold = 2.0,
                    expirationDateEpoch = now + (5 * dayMs),
                    barcode = "000000004225",
                    dailyConsumptionRate = 0.6,
                    cloudSyncStatus = "SYNCED",
                    encryptedPayloadHash = EncryptionManager.encrypt("Hass Avocados|3units")
                ),
                InventoryItemEntity(
                    name = "Whole Grain Rolled Oats",
                    category = "Pantry",
                    location = "Pantry",
                    quantity = 1.2,
                    unit = "kg",
                    minThreshold = 0.5,
                    expirationDateEpoch = now + (90 * dayMs),
                    barcode = "030000010204",
                    dailyConsumptionRate = 0.1,
                    cloudSyncStatus = "SYNCED",
                    encryptedPayloadHash = EncryptionManager.encrypt("Rolled Oats|1.2kg")
                ),
                InventoryItemEntity(
                    name = "Ground Arabica Coffee",
                    category = "Beverages",
                    location = "Pantry",
                    quantity = 0.2,
                    unit = "kg",
                    minThreshold = 0.5,
                    expirationDateEpoch = now + (60 * dayMs),
                    barcode = "076211124501",
                    dailyConsumptionRate = 0.08,
                    cloudSyncStatus = "SYNCED",
                    encryptedPayloadHash = EncryptionManager.encrypt("Ground Coffee|0.2kg")
                ),
                InventoryItemEntity(
                    name = "Free-Range Chicken Breast",
                    category = "Meat & Seafood",
                    location = "Freezer",
                    quantity = 1.5,
                    unit = "kg",
                    minThreshold = 0.8,
                    expirationDateEpoch = now + (45 * dayMs),
                    barcode = "021990003421",
                    dailyConsumptionRate = 0.25,
                    cloudSyncStatus = "SYNCED",
                    encryptedPayloadHash = EncryptionManager.encrypt("Chicken Breast|1.5kg")
                )
            )

            inventoryDao.insertItems(initialInventory)

            // Initial family shopping items
            val initialShopping = listOf(
                ShoppingItemEntity(
                    name = "Organic Whole Milk",
                    category = "Dairy & Eggs",
                    quantity = 2.0,
                    unit = "liters",
                    isPurchased = false,
                    addedByFamilyMember = "Oracle 26AI Auto-Restock",
                    priority = "HIGH",
                    estimatedPrice = 4.49
                ),
                ShoppingItemEntity(
                    name = "Pasture-Raised Eggs (12-ct)",
                    category = "Dairy & Eggs",
                    quantity = 1.0,
                    unit = "packs",
                    isPurchased = false,
                    addedByFamilyMember = "Mom",
                    priority = "HIGH",
                    estimatedPrice = 5.99
                ),
                ShoppingItemEntity(
                    name = "Organic Sourdough Loaf",
                    category = "Bakery",
                    quantity = 1.0,
                    unit = "units",
                    isPurchased = false,
                    addedByFamilyMember = "Dad",
                    priority = "NORMAL",
                    estimatedPrice = 6.29
                ),
                ShoppingItemEntity(
                    name = "Almond Milk Crisp Bars",
                    category = "Snacks",
                    quantity = 2.0,
                    unit = "boxes",
                    isPurchased = false,
                    addedByFamilyMember = "Alex",
                    priority = "LOW",
                    estimatedPrice = 4.99
                ),
                ShoppingItemEntity(
                    name = "Greek Vanilla Yogurt (32oz)",
                    category = "Dairy & Eggs",
                    quantity = 1.0,
                    unit = "tubs",
                    isPurchased = true,
                    addedByFamilyMember = "Maya",
                    purchasedByFamilyMember = "Mom",
                    priority = "NORMAL",
                    estimatedPrice = 5.49
                )
            )
            shoppingDao.insertShoppingItems(initialShopping)

            // Evaluate restock alerts
            evaluateRestockAlerts()

            // Initial sync log
            syncLogDao.insertLog(
                OracleSyncLogEntity(
                    action = "INITIAL_ORACLE_26AI_SYNC",
                    recordsCount = initialInventory.size + initialShopping.size,
                    status = "SUCCESS",
                    latencyMs = 142,
                    oracleVectorEmbeddingSignature = "ora26ai-init-vec-9921"
                )
            )
        }

        // Seed initial categories if none exist
        if (categoryDao.getCategoryCount() == 0) {
            val defaultCategories = listOf(
                CategoryEntity(name = "Produce", iconEmoji = "🥦", description = "Fresh vegetables, fruits, and fresh herbs", colorHex = "#22C55E", defaultLocation = "Fridge"),
                CategoryEntity(name = "Dairy & Eggs", iconEmoji = "🥛", description = "Milk, butter, cheeses, yogurts, and eggs", colorHex = "#3B82F6", defaultLocation = "Fridge"),
                CategoryEntity(name = "Pantry", iconEmoji = "🥫", description = "Dry grains, pasta, sauces, and canned staples", colorHex = "#F59E0B", defaultLocation = "Pantry"),
                CategoryEntity(name = "Meat & Seafood", iconEmoji = "🥩", description = "Poultry, beef, pork, and fresh catches", colorHex = "#EF4444", defaultLocation = "Fridge"),
                CategoryEntity(name = "Beverages", iconEmoji = "☕", description = "Coffee, teas, sparkling water, juices, and drinks", colorHex = "#8B5CF6", defaultLocation = "Pantry"),
                CategoryEntity(name = "Snacks", iconEmoji = "🍿", description = "Crackers, chips, nuts, bars, and dried fruit", colorHex = "#EC4899", defaultLocation = "Pantry"),
                CategoryEntity(name = "Frozen", iconEmoji = "❄️", description = "Frozen entrees, vegetables, treats, and ice", colorHex = "#06B6D4", defaultLocation = "Freezer"),
                CategoryEntity(name = "Household", iconEmoji = "🧴", description = "Cleaning supplies, wraps, and paper goods", colorHex = "#64748B", defaultLocation = "Pantry"),
                CategoryEntity(name = "Bakery", iconEmoji = "🍞", description = "Bread, bagels, artisanal loaves, and pastries", colorHex = "#D97706", defaultLocation = "Pantry"),
                CategoryEntity(name = "Spices & Oils", iconEmoji = "🧂", description = "Herbs, ground seasonings, olive oils, and vinegars", colorHex = "#14B8A6", defaultLocation = "Spice Rack"),
                CategoryEntity(name = "Uncategorized", iconEmoji = "📦", description = "General and reassigned household items", colorHex = "#94A3B8", defaultLocation = "No Specific Location")
            )
            categoryDao.insertCategories(defaultCategories)
        }

        // Seed initial locations if none exist
        if (locationDao.getLocationCount() == 0) {
            val defaultLocations = listOf(
                LocationEntity(name = "Fridge", iconEmoji = "❄️", description = "Fresh perishables, dairy, chilled beverages, and produce", colorHex = "#3B82F6", temperatureZone = "Refrigerated (37°F / 3°C)"),
                LocationEntity(name = "Pantry", iconEmoji = "🚪", description = "Dry goods, canned staples, grains, snacks, and baking goods", colorHex = "#F59E0B", temperatureZone = "Room Temp (68°F / 20°C)"),
                LocationEntity(name = "Freezer", iconEmoji = "🧊", description = "Frozen meats, seafood, frozen veggies, treats, and ice", colorHex = "#06B6D4", temperatureZone = "Deep Freeze (0°F / -18°C)"),
                LocationEntity(name = "Spice Rack", iconEmoji = "🧂", description = "Herbs, ground seasonings, oils, vinegars, and extracts", colorHex = "#10B981", temperatureZone = "Cool & Dry"),
                LocationEntity(name = "Countertop", iconEmoji = "🍞", description = "Daily fresh fruit, breads, artisanal bakery, and appliances", colorHex = "#D97706", temperatureZone = "Ambient"),
                LocationEntity(name = "Wine & Cellar", iconEmoji = "🍷", description = "Vintage bottles, aged vinegars, root veggies, and preserves", colorHex = "#8B5CF6", temperatureZone = "Cellar Cool (55°F / 13°C)"),
                LocationEntity(name = "No Specific Location", iconEmoji = "📍", description = "Items without a designated storage area", colorHex = "#94A3B8", temperatureZone = "Room Temp / Pantry")
            )
            locationDao.insertLocations(defaultLocations)
        }

        // Seed initial family members if none exist
        if (familyMemberDao.getFamilyMemberCount() == 0) {
            val defaultFamilyMembers = listOf(
                FamilyMemberEntity(name = "Mom", role = "Parent / Admin", avatarEmoji = "👩", colorHex = "#EC4899", dietaryNotes = "Low-carb, Organic preferred", preferredLanguage = "en", isDefault = true),
                FamilyMemberEntity(name = "Dad", role = "Parent", avatarEmoji = "👨", colorHex = "#3B82F6", dietaryNotes = "High-protein, Espresso enthusiast", preferredLanguage = "en", isDefault = false),
                FamilyMemberEntity(name = "Maya", role = "Teen / Student", avatarEmoji = "👧", colorHex = "#8B5CF6", dietaryNotes = "Vegetarian, Lactose-sensitive", preferredLanguage = "en", isDefault = false),
                FamilyMemberEntity(name = "Alex", role = "Child", avatarEmoji = "👦", colorHex = "#F59E0B", dietaryNotes = "Peanut allergy alert, Snack explorer", preferredLanguage = "en", isDefault = false)
            )
            familyMemberDao.insertFamilyMembers(defaultFamilyMembers)
        }
    }

    suspend fun insertInventoryItem(item: InventoryItemEntity): Long {
        val encryptedHash = EncryptionManager.encrypt("${item.name}|${item.quantity}|${item.unit}")
        val toSave = item.copy(
            encryptedPayloadHash = encryptedHash,
            cloudSyncStatus = if (oracleService.isOfflineSimulated.value) "LOCAL_CACHED" else "PENDING_PUSH"
        )
        val id = inventoryDao.insertItem(toSave)
        evaluateRestockAlerts()
        return id
    }

    suspend fun updateInventoryQuantity(id: Long, newQty: Double) {
        val clampedQty = if (newQty < 0) 0.0 else newQty
        inventoryDao.updateQuantity(id, clampedQty)
        evaluateRestockAlerts()
    }

    suspend fun updateInventoryItem(item: InventoryItemEntity) {
        inventoryDao.updateItem(item)
        evaluateRestockAlerts()
    }

    suspend fun deleteInventoryItem(item: InventoryItemEntity) {
        inventoryDao.deleteItem(item)
        alertDao.deleteAlertsForInventoryItem(item.id)
    }

    /**
     * Automatic Restocking Alert Evaluator:
     * Scans inventory for items at/below threshold or expiring soon, and creates or updates alerts.
     */
    suspend fun evaluateRestockAlerts() {
        val items = allInventoryItems.first()
        val now = System.currentTimeMillis()
        val dayMs = 86400000L

        for (item in items) {
            val daysUntilExpiry = ((item.expirationDateEpoch - now) / dayMs).toInt()

            if (item.quantity <= 0.0) {
                alertDao.deleteAlertsForInventoryItem(item.id)
                alertDao.insertAlert(
                    RestockAlertEntity(
                        inventoryItemId = item.id,
                        itemName = item.name,
                        category = item.category,
                        currentQuantity = item.quantity,
                        thresholdQuantity = item.minThreshold,
                        unit = item.unit,
                        urgency = "CRITICAL_OUT_OF_STOCK",
                        alertMessage = "${item.name} is completely out of stock! Restocking required."
                    )
                )
            } else if (item.quantity <= item.minThreshold) {
                alertDao.deleteAlertsForInventoryItem(item.id)
                alertDao.insertAlert(
                    RestockAlertEntity(
                        inventoryItemId = item.id,
                        itemName = item.name,
                        category = item.category,
                        currentQuantity = item.quantity,
                        thresholdQuantity = item.minThreshold,
                        unit = item.unit,
                        urgency = "LOW_STOCK",
                        alertMessage = "${item.name} is low (${item.quantity} / ${item.minThreshold} ${item.unit})."
                    )
                )
            } else if (daysUntilExpiry in 0..2) {
                alertDao.deleteAlertsForInventoryItem(item.id)
                alertDao.insertAlert(
                    RestockAlertEntity(
                        inventoryItemId = item.id,
                        itemName = item.name,
                        category = item.category,
                        currentQuantity = item.quantity,
                        thresholdQuantity = item.minThreshold,
                        unit = item.unit,
                        urgency = "EXPIRING_SOON",
                        alertMessage = "${item.name} expires in $daysUntilExpiry days. Consume or restock fresh."
                    )
                )
            } else {
                alertDao.deleteAlertsForInventoryItem(item.id)
            }
        }
    }

    suspend fun dismissAlert(alertId: Long) {
        alertDao.dismissAlert(alertId)
    }

    suspend fun addAlertToShoppingList(alert: RestockAlertEntity, familyMember: String = "Oracle 26AI Auto-Restock") {
        val qtyNeeded = if (alert.thresholdQuantity > alert.currentQuantity) {
            alert.thresholdQuantity * 2 - alert.currentQuantity
        } else {
            1.0
        }
        val shoppingItem = ShoppingItemEntity(
            inventoryItemId = alert.inventoryItemId,
            name = alert.itemName,
            category = alert.category,
            quantity = if (qtyNeeded <= 0) 1.0 else qtyNeeded,
            unit = alert.unit,
            isPurchased = false,
            addedByFamilyMember = familyMember,
            priority = if (alert.urgency == "CRITICAL_OUT_OF_STOCK") "HIGH" else "NORMAL",
            estimatedPrice = 4.99
        )
        shoppingDao.insertShoppingItem(shoppingItem)
        alertDao.dismissAlert(alert.id)
    }

    suspend fun addAllCriticalAlertsToShoppingList() {
        val alerts = activeAlerts.first()
        for (alert in alerts) {
            addAlertToShoppingList(alert)
        }
    }

    // Shopping List Operations
    suspend fun insertShoppingItem(item: ShoppingItemEntity): Long {
        return shoppingDao.insertShoppingItem(
            item.copy(cloudSyncStatus = if (oracleService.isOfflineSimulated.value) "LOCAL_CACHED" else "PENDING_PUSH")
        )
    }

    suspend fun updateShoppingItem(item: ShoppingItemEntity) {
        shoppingDao.updateShoppingItem(
            item.copy(cloudSyncStatus = if (oracleService.isOfflineSimulated.value) "LOCAL_CACHED" else "PENDING_PUSH")
        )
    }

    suspend fun toggleShoppingItemPurchased(id: Long, isPurchased: Boolean, member: String = "Family") {
        shoppingDao.setPurchased(id, isPurchased, if (isPurchased) member else null)
    }

    suspend fun deleteShoppingItem(item: ShoppingItemEntity) {
        shoppingDao.deleteShoppingItem(item)
    }

    suspend fun markBoughtAndRestockInventory(shoppingItem: ShoppingItemEntity) {
        shoppingDao.setPurchased(shoppingItem.id, true, shoppingItem.addedByFamilyMember)

        // Find linked or matching inventory item and restock
        val allItems = allInventoryItems.first()
        val matching = allItems.find {
            it.id == shoppingItem.inventoryItemId || it.name.equals(shoppingItem.name, ignoreCase = true)
        }

        if (matching != null) {
            val newQty = matching.quantity + shoppingItem.quantity
            inventoryDao.updateQuantity(matching.id, newQty)
            alertDao.deleteAlertsForInventoryItem(matching.id)
        } else {
            // Create new inventory item
            insertInventoryItem(
                InventoryItemEntity(
                    name = shoppingItem.name,
                    category = shoppingItem.category,
                    location = "Pantry",
                    quantity = shoppingItem.quantity,
                    unit = shoppingItem.unit,
                    minThreshold = 1.0,
                    expirationDateEpoch = System.currentTimeMillis() + (14 * 86400000L),
                    dailyConsumptionRate = 0.2
                )
            )
        }
    }

    suspend fun clearCompletedShoppingItems() {
        shoppingDao.clearCompletedItems()
    }

    // Oracle 26AI Cloud Sync
    suspend fun syncWithOracle26Ai(): SyncResult {
        val inventory = allInventoryItems.first()
        val shopping = allShoppingItems.first()
        val result = oracleService.performSync(inventory, shopping)

        syncLogDao.insertLog(
            OracleSyncLogEntity(
                action = if (result.success) "ORACLE_26AI_PUSH_PULL" else "OFFLINE_CACHE_EVENT",
                recordsCount = result.itemsPushed,
                status = if (result.success) "SUCCESS" else "OFFLINE_CACHED",
                latencyMs = result.latencyMs,
                oracleVectorEmbeddingSignature = result.vectorEmbeddingSignature
            )
        )
        return result
    }

    // Predictive Analytics Report
    suspend fun getPredictiveAnalyticsReport(): PredictiveAnalyticsReport {
        val items = allInventoryItems.first()
        return PredictiveAnalyticsEngine.generateReport(items)
    }

    // Category CRUD operations with cascade reassignments
    suspend fun insertCategory(category: CategoryEntity): Long {
        return categoryDao.insertCategory(category)
    }

    suspend fun updateCategory(category: CategoryEntity) {
        categoryDao.updateCategory(category)
    }

    suspend fun deleteCategory(category: CategoryEntity) {
        // Cascade update: reassign associated items to "Uncategorized"
        inventoryDao.updateItemsCategory(category.name, "Uncategorized")
        shoppingDao.updateShoppingItemsCategory(category.name, "Uncategorized")
        categoryDao.deleteCategory(category)
    }

    suspend fun deleteCategoryById(id: Long) {
        categoryDao.deleteCategoryById(id)
    }

    // Location CRUD operations with cascade reassignments
    suspend fun insertLocation(location: LocationEntity): Long {
        return locationDao.insertLocation(location)
    }

    suspend fun updateLocation(location: LocationEntity) {
        locationDao.updateLocation(location)
    }

    suspend fun deleteLocation(location: LocationEntity) {
        // Cascade update: reassign associated items and category default locations
        inventoryDao.updateItemsLocation(location.name, "No Specific Location")
        categoryDao.updateCategoriesDefaultLocation(location.name, "No Specific Location")
        locationDao.deleteLocation(location)
    }

    suspend fun deleteLocationById(id: Long) {
        locationDao.deleteLocationById(id)
    }

    // Family Member CRUD operations
    suspend fun insertFamilyMember(member: FamilyMemberEntity): Long {
        return familyMemberDao.insertFamilyMember(member)
    }

    suspend fun updateFamilyMember(member: FamilyMemberEntity) {
        familyMemberDao.updateFamilyMember(member)
    }

    suspend fun deleteFamilyMember(member: FamilyMemberEntity) {
        familyMemberDao.deleteFamilyMember(member)
    }

    suspend fun deleteFamilyMemberById(id: Long) {
        familyMemberDao.deleteFamilyMemberById(id)
    }

    // Temperature Zone CRUD operations
    suspend fun insertTemperatureZone(zone: TemperatureZoneEntity): Long {
        return temperatureZoneDao.insertZone(zone)
    }

    suspend fun updateTemperatureZone(zone: TemperatureZoneEntity) {
        temperatureZoneDao.updateZone(zone)
    }

    suspend fun deleteTemperatureZone(zone: TemperatureZoneEntity) {
        temperatureZoneDao.deleteZone(zone)
    }

    suspend fun deleteTemperatureZoneById(id: Long) {
        temperatureZoneDao.deleteZoneById(id)
    }

    // User Preferences & Settings Persistence
    suspend fun saveUserPreference(preference: UserPreferenceEntity) {
        userPreferenceDao.savePreference(preference)
    }
}
