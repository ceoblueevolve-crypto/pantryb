package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.data.oracle.OracleConnectionConfig
import com.example.data.oracle.PredictiveAnalyticsEngine
import com.example.data.oracle.PredictiveAnalyticsReport
import com.example.data.oracle.SyncResult
import com.example.data.repository.PantryRepository
import com.example.data.security.BiometricAuthManager
import com.example.data.security.CiphertextInspection
import com.example.data.security.EncryptionManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PantryUiState(
    val inventoryItems: List<InventoryItemEntity> = emptyList(),
    val shoppingItems: List<ShoppingItemEntity> = emptyList(),
    val restockAlerts: List<RestockAlertEntity> = emptyList(),
    val syncLogs: List<OracleSyncLogEntity> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    val locations: List<LocationEntity> = emptyList(),
    val temperatureZones: List<TemperatureZoneEntity> = emptyList(),
    val familyMembers: List<FamilyMemberEntity> = emptyList(),
    val analyticsReport: PredictiveAnalyticsReport? = null,
    val selectedCategory: String = "All",
    val selectedLocation: String = "All",
    val selectedTemperatureZoneFilter: String = "All",
    val searchQuery: String = "",
    val selectedFamilyMemberFilter: String = "All",
    val isSyncing: Boolean = false,
    val lastSyncResult: SyncResult? = null,
    val isOfflineMode: Boolean = false,
    val connectionState: CloudConnectionState = CloudConnectionState.CONNECTED,
    val activeFamilyMember: String = "Mom",
    val userPinInput: String = "",
    val pinError: String? = null,
    val inspectedCiphertext: CiphertextInspection? = null,
    val showAddItemDialog: Boolean = false,
    val showEditItemDialog: Boolean = false,
    val itemToEdit: InventoryItemEntity? = null,
    val showSplash: Boolean = true,
    val showAddShoppingDialog: Boolean = false,
    val showEditShoppingDialog: Boolean = false,
    val shoppingItemToEdit: ShoppingItemEntity? = null,
    val showOracleConfigDialog: Boolean = false,
    val showCategoryDialog: Boolean = false,
    val categoryToEdit: CategoryEntity? = null,
    val showLocationDialog: Boolean = false,
    val locationToEdit: LocationEntity? = null,
    val showTemperatureZoneDialog: Boolean = false,
    val temperatureZoneToEdit: TemperatureZoneEntity? = null,
    val showFamilyMemberDialog: Boolean = false,
    val familyMemberToEdit: FamilyMemberEntity? = null,
    val activeTab: Int = 0,
    val settingsSubTab: Int = 0,
    val selectedLanguage: String = "en",
    val autoDetectSystemLanguage: Boolean = true,
    val enableAiAutoTranslation: Boolean = true,
    val familyMemberLanguages: Map<String, String> = mapOf(
        "Mom" to "en",
        "Dad" to "es",
        "Teenager" to "en",
        "Kid" to "en"
    ),
    val measurementSystem: String = "Metric (kg, g, L)",
    val currencySymbol: String = "$"
)

data class SupportedLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val flagEmoji: String,
    val region: String,
    val sampleGreeting: String
)

val AVAILABLE_LANGUAGES = listOf(
    SupportedLanguage("en", "English", "English (US)", "🇺🇸", "United States", "Welcome to your smart pantry!"),
    SupportedLanguage("es", "Spanish", "Español", "🇪🇸", "Spain & Latin America", "¡Bienvenido a tu despensa inteligente!"),
    SupportedLanguage("fr", "French", "Français", "🇫🇷", "France & Canada", "Bienvenue dans votre garde-manger !"),
    SupportedLanguage("de", "German", "Deutsch", "🇩🇪", "Germany & Austria", "Willkommen in Ihrer Speisekammer!"),
    SupportedLanguage("it", "Italian", "Italiano", "🇮🇹", "Italy", "Benvenuto nella tua dispensa intelligente!"),
    SupportedLanguage("pt", "Portuguese", "Português", "🇧🇷", "Brazil & Portugal", "Bem-vindo à sua despensa inteligente!"),
    SupportedLanguage("ja", "Japanese", "日本語", "🇯🇵", "Japan", "スマートパントリーへようこそ！"),
    SupportedLanguage("zh", "Chinese", "简体中文", "🇨🇳", "China", "欢迎使用智能食品储藏室！"),
    SupportedLanguage("ko", "Korean", "한국어", "🇰🇷", "South Korea", "스마트 팬트리에 오신 것을 환영합니다!"),
    SupportedLanguage("ar", "Arabic", "العربية", "🇸🇦", "Middle East", "مرحبًا بك في مخزنك الذكي!")
)

class PantryViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val oracleService = Oracle26AiService()
    val repository = PantryRepository(database, oracleService)
    val biometricManager = BiometricAuthManager(application)

    private val _uiState = MutableStateFlow(PantryUiState())
    val uiState: StateFlow<PantryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }

        // Observe Inventory
        viewModelScope.launch {
            repository.allInventoryItems.collect { items ->
                val report = PredictiveAnalyticsEngine.generateReport(items)
                _uiState.update { it.copy(inventoryItems = items, analyticsReport = report) }
            }
        }

        // Observe Shopping List
        viewModelScope.launch {
            repository.allShoppingItems.collect { shopping ->
                _uiState.update { it.copy(shoppingItems = shopping) }
            }
        }

        // Observe Alerts
        viewModelScope.launch {
            repository.activeAlerts.collect { alerts ->
                _uiState.update { it.copy(restockAlerts = alerts) }
            }
        }

        // Observe Sync Logs
        viewModelScope.launch {
            repository.syncLogs.collect { logs ->
                _uiState.update { it.copy(syncLogs = logs) }
            }
        }

        // Observe Oracle Cloud Connection State
        viewModelScope.launch {
            oracleService.connectionState.collect { state ->
                _uiState.update { it.copy(connectionState = state) }
            }
        }

        // Observe Offline Mode state
        viewModelScope.launch {
            oracleService.isOfflineSimulated.collect { offline ->
                _uiState.update { it.copy(isOfflineMode = offline) }
            }
        }

        // Observe Categories
        viewModelScope.launch {
            repository.allCategories.collect { cats ->
                _uiState.update { it.copy(categories = cats) }
            }
        }

        // Observe Locations
        viewModelScope.launch {
            repository.allLocations.collect { locs ->
                _uiState.update { it.copy(locations = locs) }
            }
        }

        // Observe Temperature Zones
        viewModelScope.launch {
            repository.allTemperatureZones.collect { zones ->
                _uiState.update { it.copy(temperatureZones = zones) }
            }
        }

        // Observe Family Members
        viewModelScope.launch {
            repository.allFamilyMembers.collect { members ->
                _uiState.update { state ->
                    val langMap = state.familyMemberLanguages.toMutableMap()
                    members.forEach { m ->
                        if (!langMap.containsKey(m.name)) {
                            langMap[m.name] = m.preferredLanguage
                        }
                    }
                    val defaultMember = members.find { it.isDefault }?.name ?: (members.firstOrNull()?.name ?: state.activeFamilyMember)
                    val activeMem = if (members.isNotEmpty() && members.none { it.name == state.activeFamilyMember }) defaultMember else state.activeFamilyMember
                    state.copy(
                        familyMembers = members,
                        familyMemberLanguages = langMap,
                        activeFamilyMember = activeMem
                    )
                }
            }
        }

        // Observe User Preferences from Room Database
        viewModelScope.launch {
            repository.userPreference.collect { pref ->
                if (pref != null) {
                    _uiState.update { state ->
                        state.copy(
                            selectedLanguage = pref.selectedLanguage,
                            autoDetectSystemLanguage = pref.autoDetectLanguage,
                            measurementSystem = pref.measurementSystem,
                            currencySymbol = pref.currencySymbol,
                            enableAiAutoTranslation = pref.enableAiTranslation
                        )
                    }
                }
            }
        }
    }

    private fun persistUserPreferences() {
        viewModelScope.launch {
            val state = _uiState.value
            repository.saveUserPreference(
                UserPreferenceEntity(
                    profileName = state.activeFamilyMember.ifBlank { "Default" },
                    selectedLanguage = state.selectedLanguage,
                    autoDetectLanguage = state.autoDetectSystemLanguage,
                    measurementSystem = state.measurementSystem,
                    currencySymbol = state.currencySymbol,
                    enableAiTranslation = state.enableAiAutoTranslation
                )
            )
        }
    }

    fun selectTab(index: Int) {
        _uiState.update { it.copy(activeTab = index) }
    }

    fun selectSettingsSubTab(subTab: Int) {
        _uiState.update { it.copy(activeTab = 5, settingsSubTab = subTab) }
    }

    fun setLanguage(code: String) {
        _uiState.update { it.copy(selectedLanguage = code, autoDetectSystemLanguage = false) }
        persistUserPreferences()
    }

    fun setAutoDetectSystemLanguage(enabled: Boolean) {
        _uiState.update { it.copy(autoDetectSystemLanguage = enabled) }
        persistUserPreferences()
    }

    fun setAiAutoTranslation(enabled: Boolean) {
        _uiState.update { it.copy(enableAiAutoTranslation = enabled) }
        persistUserPreferences()
    }

    fun setFamilyMemberLanguage(member: String, langCode: String) {
        _uiState.update {
            val updated = it.familyMemberLanguages.toMutableMap()
            updated[member] = langCode
            it.copy(familyMemberLanguages = updated)
        }
    }

    fun setMeasurementSystem(system: String) {
        _uiState.update { it.copy(measurementSystem = system) }
        persistUserPreferences()
    }

    fun setCurrencySymbol(symbol: String) {
        _uiState.update { it.copy(currencySymbol = symbol) }
        persistUserPreferences()
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setCategoryFilter(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setLocationFilter(location: String) {
        _uiState.update { it.copy(selectedLocation = location) }
    }

    fun setFamilyMemberFilter(member: String) {
        _uiState.update { it.copy(selectedFamilyMemberFilter = member) }
    }

    fun setActiveFamilyMember(member: String) {
        _uiState.update { it.copy(activeFamilyMember = member) }
    }

    // Inventory Actions
    fun adjustQuantity(item: InventoryItemEntity, delta: Double) {
        viewModelScope.launch {
            val newQty = (item.quantity + delta).coerceAtLeast(0.0)
            repository.updateInventoryQuantity(item.id, (newQty * 10).toInt() / 10.0)
        }
    }

    fun addInventoryItem(
        name: String,
        category: String,
        location: String,
        quantity: Double,
        unit: String,
        minThreshold: Double,
        daysUntilExpiry: Int
    ) {
        viewModelScope.launch {
            val expiryEpoch = System.currentTimeMillis() + (daysUntilExpiry * 86400000L)
            repository.insertInventoryItem(
                InventoryItemEntity(
                    name = name,
                    category = category,
                    location = location,
                    quantity = quantity,
                    unit = unit,
                    minThreshold = minThreshold,
                    expirationDateEpoch = expiryEpoch,
                    dailyConsumptionRate = (quantity / 10.0).coerceAtLeast(0.1)
                )
            )
            _uiState.update { it.copy(showAddItemDialog = false) }
        }
    }

    fun openEditInventoryItemDialog(item: InventoryItemEntity) {
        _uiState.update { it.copy(showEditItemDialog = true, itemToEdit = item) }
    }

    fun dismissEditInventoryItemDialog() {
        _uiState.update { it.copy(showEditItemDialog = false, itemToEdit = null) }
    }

    fun updateInventoryItem(
        id: Long,
        name: String,
        category: String,
        location: String,
        quantity: Double,
        unit: String,
        minThreshold: Double,
        daysUntilExpiry: Int
    ) {
        viewModelScope.launch {
            val existing = _uiState.value.inventoryItems.find { it.id == id }
            val expiryEpoch = if (daysUntilExpiry > 0) {
                System.currentTimeMillis() + (daysUntilExpiry * 86400000L)
            } else {
                existing?.expirationDateEpoch ?: (System.currentTimeMillis() + 7 * 86400000L)
            }
            val updated = (existing ?: InventoryItemEntity(
                name = name,
                category = category,
                location = location,
                quantity = quantity,
                unit = unit,
                minThreshold = minThreshold,
                expirationDateEpoch = expiryEpoch
            )).copy(
                id = id,
                name = name.trim(),
                category = category,
                location = location,
                quantity = quantity,
                unit = unit,
                minThreshold = minThreshold,
                expirationDateEpoch = expiryEpoch,
                dailyConsumptionRate = (quantity / 10.0).coerceAtLeast(0.1)
            )
            repository.updateInventoryItem(updated)
            _uiState.update { it.copy(showEditItemDialog = false, itemToEdit = null) }
        }
    }

    fun dismissSplash() {
        _uiState.update { it.copy(showSplash = false) }
    }

    fun deleteInventoryItem(item: InventoryItemEntity) {
        viewModelScope.launch {
            repository.deleteInventoryItem(item)
        }
    }

    // Shopping List Actions
    fun toggleShoppingItem(item: ShoppingItemEntity) {
        viewModelScope.launch {
            repository.toggleShoppingItemPurchased(
                item.id,
                !item.isPurchased,
                _uiState.value.activeFamilyMember
            )
        }
    }

    fun markBoughtAndRestock(item: ShoppingItemEntity) {
        viewModelScope.launch {
            repository.markBoughtAndRestockInventory(item)
        }
    }

    fun addShoppingItem(
        name: String,
        category: String,
        quantity: Double,
        unit: String,
        priority: String,
        price: Double
    ) {
        viewModelScope.launch {
            repository.insertShoppingItem(
                ShoppingItemEntity(
                    name = name,
                    category = category,
                    quantity = quantity,
                    unit = unit,
                    isPurchased = false,
                    addedByFamilyMember = _uiState.value.activeFamilyMember,
                    priority = priority,
                    estimatedPrice = price
                )
            )
            _uiState.update { it.copy(showAddShoppingDialog = false) }
        }
    }

    fun openEditShoppingDialog(item: ShoppingItemEntity) {
        _uiState.update { it.copy(showEditShoppingDialog = true, shoppingItemToEdit = item) }
    }

    fun dismissEditShoppingDialog() {
        _uiState.update { it.copy(showEditShoppingDialog = false, shoppingItemToEdit = null) }
    }

    fun updateShoppingItem(
        id: Long,
        name: String,
        category: String,
        quantity: Double,
        unit: String,
        priority: String,
        price: Double,
        requestedBy: String
    ) {
        viewModelScope.launch {
            val existing = _uiState.value.shoppingItems.find { it.id == id }
            val updated = existing?.copy(
                name = name,
                category = category,
                quantity = quantity,
                unit = unit,
                priority = priority,
                estimatedPrice = price,
                addedByFamilyMember = requestedBy
            ) ?: ShoppingItemEntity(
                id = id,
                name = name,
                category = category,
                quantity = quantity,
                unit = unit,
                priority = priority,
                estimatedPrice = price,
                addedByFamilyMember = requestedBy
            )
            repository.updateShoppingItem(updated)
            _uiState.update { it.copy(showEditShoppingDialog = false, shoppingItemToEdit = null) }
        }
    }

    fun deleteShoppingItem(item: ShoppingItemEntity) {
        viewModelScope.launch {
            repository.deleteShoppingItem(item)
        }
    }

    fun clearCompletedShopping() {
        viewModelScope.launch {
            repository.clearCompletedShoppingItems()
        }
    }

    // Restock Alerts Actions
    fun dismissAlert(alert: RestockAlertEntity) {
        viewModelScope.launch {
            repository.dismissAlert(alert.id)
        }
    }

    fun addAlertToShopping(alert: RestockAlertEntity) {
        viewModelScope.launch {
            repository.addAlertToShoppingList(alert, _uiState.value.activeFamilyMember)
        }
    }

    fun addAllCriticalAlertsToShopping() {
        viewModelScope.launch {
            repository.addAllCriticalAlertsToShoppingList()
        }
    }

    // Oracle 26AI Cloud Sync Actions
    fun triggerOracleSync() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            val result = repository.syncWithOracle26Ai()
            _uiState.update { it.copy(isSyncing = false, lastSyncResult = result) }
        }
    }

    fun toggleOfflineMode() {
        oracleService.toggleOfflineMode()
    }

    fun updateOracleConfig(config: OracleConnectionConfig) {
        oracleService.updateConfig(config)
        _uiState.update { it.copy(showOracleConfigDialog = false) }
    }

    // Security & Biometric Actions
    fun unlockWithBiometrics() {
        biometricManager.authenticateWithBiometrics(
            onSuccess = {
                _uiState.update { it.copy(pinError = null, userPinInput = "") }
            },
            onError = { err ->
                _uiState.update { it.copy(pinError = err) }
            }
        )
    }

    fun unlockWithPin(pin: String) {
        val success = biometricManager.authenticateWithPin(pin)
        if (success) {
            _uiState.update { it.copy(pinError = null, userPinInput = "") }
        } else {
            _uiState.update { it.copy(pinError = "Incorrect PIN. Default is 1234") }
        }
    }

    fun lockApp() {
        biometricManager.lockApp()
        _uiState.update { it.copy(userPinInput = "") }
    }

    fun rotateEncryptionKey() {
        val newFingerprint = EncryptionManager.generateNewHouseholdKey()
        val sampleCiphertext = EncryptionManager.encrypt("HouseholdInventoryVault|Token:${System.currentTimeMillis()}")
        val inspection = EncryptionManager.inspectCiphertext(sampleCiphertext)
        _uiState.update { it.copy(inspectedCiphertext = inspection) }
    }

    fun inspectItemCiphertext(item: InventoryItemEntity) {
        val ciphertext = if (item.encryptedPayloadHash.isNotEmpty()) {
            item.encryptedPayloadHash
        } else {
            EncryptionManager.encrypt("${item.name}|${item.quantity}${item.unit}")
        }
        val inspection = EncryptionManager.inspectCiphertext(ciphertext)
        _uiState.update { it.copy(inspectedCiphertext = inspection) }
    }

    fun setPinInput(pin: String) {
        _uiState.update { it.copy(userPinInput = pin, pinError = null) }
    }

    fun setShowAddItemDialog(show: Boolean) {
        _uiState.update { it.copy(showAddItemDialog = show) }
    }

    fun setShowAddShoppingDialog(show: Boolean) {
        _uiState.update { it.copy(showAddShoppingDialog = show) }
    }

    fun setShowOracleConfigDialog(show: Boolean) {
        _uiState.update { it.copy(showOracleConfigDialog = show) }
    }

    // Category CRUD actions
    fun openAddCategoryDialog() {
        _uiState.update { it.copy(showCategoryDialog = true, categoryToEdit = null) }
    }

    fun openEditCategoryDialog(category: CategoryEntity) {
        _uiState.update { it.copy(showCategoryDialog = true, categoryToEdit = category) }
    }

    fun dismissCategoryDialog() {
        _uiState.update { it.copy(showCategoryDialog = false, categoryToEdit = null) }
    }

    fun saveCategory(
        name: String,
        iconEmoji: String,
        description: String,
        colorHex: String,
        defaultLocation: String,
        id: Long = 0
    ) {
        viewModelScope.launch {
            val category = CategoryEntity(
                id = id,
                name = name.trim(),
                iconEmoji = iconEmoji.ifBlank { "📦" },
                description = description.trim(),
                colorHex = colorHex.ifBlank { "#10B981" },
                defaultLocation = defaultLocation
            )
            if (id == 0L) {
                repository.insertCategory(category)
            } else {
                repository.updateCategory(category)
            }
            _uiState.update { it.copy(showCategoryDialog = false, categoryToEdit = null) }
        }
    }

    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            repository.deleteCategory(category)
            // If current category filter is deleted category, reset to "All"
            if (_uiState.value.selectedCategory == category.name) {
                _uiState.update { it.copy(selectedCategory = "All") }
            }
        }
    }

    // Location Management Operations
    fun openAddLocationDialog() {
        _uiState.update { it.copy(showLocationDialog = true, locationToEdit = null) }
    }

    fun openEditLocationDialog(location: LocationEntity) {
        _uiState.update { it.copy(showLocationDialog = true, locationToEdit = location) }
    }

    fun dismissLocationDialog() {
        _uiState.update { it.copy(showLocationDialog = false, locationToEdit = null) }
    }

    fun saveLocation(
        name: String,
        iconEmoji: String,
        description: String,
        colorHex: String,
        temperatureZone: String,
        id: Long = 0
    ) {
        viewModelScope.launch {
            val location = LocationEntity(
                id = id,
                name = name.trim(),
                iconEmoji = iconEmoji.ifBlank { "🏠" },
                description = description.trim(),
                colorHex = colorHex.ifBlank { "#3B82F6" },
                temperatureZone = temperatureZone
            )
            if (id == 0L) {
                repository.insertLocation(location)
            } else {
                repository.updateLocation(location)
            }
            _uiState.update { it.copy(showLocationDialog = false, locationToEdit = null) }
        }
    }

    fun deleteLocation(location: LocationEntity) {
        viewModelScope.launch {
            repository.deleteLocation(location)
            // If current location filter is deleted location, reset to "All"
            if (_uiState.value.selectedLocation == location.name) {
                _uiState.update { it.copy(selectedLocation = "All") }
            }
        }
    }

    // Family Member Management Operations
    fun openAddFamilyMemberDialog() {
        _uiState.update { it.copy(showFamilyMemberDialog = true, familyMemberToEdit = null) }
    }

    fun openEditFamilyMemberDialog(member: FamilyMemberEntity) {
        _uiState.update { it.copy(showFamilyMemberDialog = true, familyMemberToEdit = member) }
    }

    fun dismissFamilyMemberDialog() {
        _uiState.update { it.copy(showFamilyMemberDialog = false, familyMemberToEdit = null) }
    }

    fun saveFamilyMember(
        name: String,
        role: String,
        avatarEmoji: String,
        colorHex: String,
        dietaryNotes: String,
        preferredLanguage: String,
        isDefault: Boolean,
        id: Long = 0
    ) {
        viewModelScope.launch {
            val member = FamilyMemberEntity(
                id = id,
                name = name.trim(),
                role = role.trim().ifBlank { "Family Member" },
                avatarEmoji = avatarEmoji.ifBlank { "👤" },
                colorHex = colorHex.ifBlank { "#3B82F6" },
                dietaryNotes = dietaryNotes.trim(),
                preferredLanguage = preferredLanguage.ifBlank { "en" },
                isDefault = isDefault
            )
            if (id == 0L) {
                repository.insertFamilyMember(member)
            } else {
                repository.updateFamilyMember(member)
            }
            if (isDefault) {
                _uiState.update { it.copy(activeFamilyMember = member.name) }
            }
            _uiState.update { it.copy(showFamilyMemberDialog = false, familyMemberToEdit = null) }
        }
    }

    fun deleteFamilyMember(member: FamilyMemberEntity) {
        viewModelScope.launch {
            repository.deleteFamilyMember(member)
            // If active profile or filter was deleted member, reset gracefully
            val remaining = _uiState.value.familyMembers.filter { it.id != member.id }
            val nextActive = remaining.firstOrNull()?.name ?: "Mom"
            _uiState.update { state ->
                state.copy(
                    activeFamilyMember = if (state.activeFamilyMember == member.name) nextActive else state.activeFamilyMember,
                    selectedFamilyMemberFilter = if (state.selectedFamilyMemberFilter == member.name) "All" else state.selectedFamilyMemberFilter
                )
            }
        }
    }

    // Temperature Zone Management Operations
    fun openAddTemperatureZoneDialog() {
        _uiState.update { it.copy(showTemperatureZoneDialog = true, temperatureZoneToEdit = null) }
    }

    fun openEditTemperatureZoneDialog(zone: TemperatureZoneEntity) {
        _uiState.update { it.copy(showTemperatureZoneDialog = true, temperatureZoneToEdit = zone) }
    }

    fun dismissTemperatureZoneDialog() {
        _uiState.update { it.copy(showTemperatureZoneDialog = false, temperatureZoneToEdit = null) }
    }

    fun saveTemperatureZone(
        name: String,
        iconEmoji: String,
        description: String,
        colorHex: String,
        targetTemperature: String,
        id: Long = 0
    ) {
        viewModelScope.launch {
            val zone = TemperatureZoneEntity(
                id = id,
                name = name.trim(),
                iconEmoji = iconEmoji.ifBlank { "🌡️" },
                description = description.trim(),
                colorHex = colorHex.ifBlank { "#3B82F6" },
                targetRange = targetTemperature.trim().ifBlank { "Ambient" }
            )
            if (id == 0L) {
                repository.insertTemperatureZone(zone)
            } else {
                repository.updateTemperatureZone(zone)
            }
            _uiState.update { it.copy(showTemperatureZoneDialog = false, temperatureZoneToEdit = null) }
        }
    }

    fun deleteTemperatureZone(zone: TemperatureZoneEntity) {
        viewModelScope.launch {
            repository.deleteTemperatureZone(zone)
            if (_uiState.value.selectedTemperatureZoneFilter == zone.name) {
                _uiState.update { it.copy(selectedTemperatureZoneFilter = "All") }
            }
        }
    }

    fun filterByTemperatureZone(zoneName: String) {
        _uiState.update { it.copy(selectedTemperatureZoneFilter = zoneName) }
    }
}
