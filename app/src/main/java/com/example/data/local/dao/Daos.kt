package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.FamilyMemberEntity
import com.example.data.local.entity.InventoryItemEntity
import com.example.data.local.entity.LocationEntity
import com.example.data.local.entity.OracleSyncLogEntity
import com.example.data.local.entity.RestockAlertEntity
import com.example.data.local.entity.ShoppingItemEntity
import com.example.data.local.entity.TemperatureZoneEntity
import com.example.data.local.entity.UserPreferenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {
    @Query("SELECT * FROM inventory_items ORDER BY name ASC")
    fun getAllInventoryItems(): Flow<List<InventoryItemEntity>>

    @Query("SELECT * FROM inventory_items WHERE id = :id LIMIT 1")
    suspend fun getItemById(id: Long): InventoryItemEntity?

    @Query("SELECT * FROM inventory_items WHERE quantity <= minThreshold")
    fun getLowStockItems(): Flow<List<InventoryItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: InventoryItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<InventoryItemEntity>)

    @Update
    suspend fun updateItem(item: InventoryItemEntity)

    @Delete
    suspend fun deleteItem(item: InventoryItemEntity)

    @Query("DELETE FROM inventory_items WHERE id = :id")
    suspend fun deleteItemById(id: Long)

    @Query("UPDATE inventory_items SET quantity = :newQty, cloudSyncStatus = 'PENDING_PUSH' WHERE id = :id")
    suspend fun updateQuantity(id: Long, newQty: Double)

    @Query("UPDATE inventory_items SET category = :newCategory, cloudSyncStatus = 'PENDING_PUSH' WHERE category = :oldCategory")
    suspend fun updateItemsCategory(oldCategory: String, newCategory: String)

    @Query("UPDATE inventory_items SET location = :newLocation, cloudSyncStatus = 'PENDING_PUSH' WHERE location = :oldLocation")
    suspend fun updateItemsLocation(oldLocation: String, newLocation: String)
}

@Dao
interface ShoppingDao {
    @Query("SELECT * FROM shopping_items ORDER BY isPurchased ASC, createdEpoch DESC")
    fun getAllShoppingItems(): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(item: ShoppingItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItems(items: List<ShoppingItemEntity>)

    @Update
    suspend fun updateShoppingItem(item: ShoppingItemEntity)

    @Delete
    suspend fun deleteShoppingItem(item: ShoppingItemEntity)

    @Query("DELETE FROM shopping_items WHERE id = :id")
    suspend fun deleteShoppingItemById(id: Long)

    @Query("UPDATE shopping_items SET isPurchased = :purchased, purchasedByFamilyMember = :member, cloudSyncStatus = 'PENDING_PUSH' WHERE id = :id")
    suspend fun setPurchased(id: Long, purchased: Boolean, member: String?)

    @Query("UPDATE shopping_items SET category = :newCategory WHERE category = :oldCategory")
    suspend fun updateShoppingItemsCategory(oldCategory: String, newCategory: String)

    @Query("DELETE FROM shopping_items WHERE isPurchased = 1")
    suspend fun clearCompletedItems()
}

@Dao
interface AlertDao {
    @Query("SELECT * FROM restock_alerts WHERE isDismissed = 0 ORDER BY createdEpoch DESC")
    fun getActiveAlerts(): Flow<List<RestockAlertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: RestockAlertEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlerts(alerts: List<RestockAlertEntity>)

    @Query("UPDATE restock_alerts SET isDismissed = 1 WHERE id = :id")
    suspend fun dismissAlert(id: Long)

    @Query("DELETE FROM restock_alerts WHERE inventoryItemId = :inventoryId")
    suspend fun deleteAlertsForInventoryItem(inventoryId: Long)

    @Query("DELETE FROM restock_alerts")
    suspend fun clearAllAlerts()
}

@Dao
interface SyncLogDao {
    @Query("SELECT * FROM oracle_sync_logs ORDER BY timestampEpoch DESC LIMIT 50")
    fun getRecentLogs(): Flow<List<OracleSyncLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: OracleSyncLogEntity)

    @Query("DELETE FROM oracle_sync_logs")
    suspend fun clearLogs()
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM product_categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM product_categories WHERE id = :id LIMIT 1")
    suspend fun getCategoryById(id: Long): CategoryEntity?

    @Query("SELECT * FROM product_categories WHERE name = :name LIMIT 1")
    suspend fun getCategoryByName(name: String): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("DELETE FROM product_categories WHERE id = :id")
    suspend fun deleteCategoryById(id: Long)

    @Query("UPDATE product_categories SET defaultLocation = :newLocation WHERE defaultLocation = :oldLocation")
    suspend fun updateCategoriesDefaultLocation(oldLocation: String, newLocation: String)

    @Query("SELECT COUNT(*) FROM product_categories")
    suspend fun getCategoryCount(): Int
}

@Dao
interface LocationDao {
    @Query("SELECT * FROM storage_locations ORDER BY name ASC")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM storage_locations WHERE id = :id LIMIT 1")
    suspend fun getLocationById(id: Long): LocationEntity?

    @Query("SELECT * FROM storage_locations WHERE name = :name LIMIT 1")
    suspend fun getLocationByName(name: String): LocationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(locations: List<LocationEntity>)

    @Update
    suspend fun updateLocation(location: LocationEntity)

    @Delete
    suspend fun deleteLocation(location: LocationEntity)

    @Query("DELETE FROM storage_locations WHERE id = :id")
    suspend fun deleteLocationById(id: Long)

    @Query("SELECT COUNT(*) FROM storage_locations")
    suspend fun getLocationCount(): Int
}

@Dao
interface FamilyMemberDao {
    @Query("SELECT * FROM family_members ORDER BY isDefault DESC, name ASC")
    fun getAllFamilyMembers(): Flow<List<FamilyMemberEntity>>

    @Query("SELECT * FROM family_members WHERE id = :id LIMIT 1")
    suspend fun getFamilyMemberById(id: Long): FamilyMemberEntity?

    @Query("SELECT * FROM family_members WHERE name = :name LIMIT 1")
    suspend fun getFamilyMemberByName(name: String): FamilyMemberEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilyMember(member: FamilyMemberEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilyMembers(members: List<FamilyMemberEntity>)

    @Update
    suspend fun updateFamilyMember(member: FamilyMemberEntity)

    @Delete
    suspend fun deleteFamilyMember(member: FamilyMemberEntity)

    @Query("DELETE FROM family_members WHERE id = :id")
    suspend fun deleteFamilyMemberById(id: Long)

    @Query("SELECT COUNT(*) FROM family_members")
    suspend fun getFamilyMemberCount(): Int
}

@Dao
interface TemperatureZoneDao {
    @Query("SELECT * FROM temperature_zones ORDER BY name ASC")
    fun getAllTemperatureZones(): Flow<List<TemperatureZoneEntity>>

    @Query("SELECT * FROM temperature_zones WHERE id = :id LIMIT 1")
    suspend fun getZoneById(id: Long): TemperatureZoneEntity?

    @Query("SELECT * FROM temperature_zones WHERE name = :name LIMIT 1")
    suspend fun getZoneByName(name: String): TemperatureZoneEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZone(zone: TemperatureZoneEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZones(zones: List<TemperatureZoneEntity>)

    @Update
    suspend fun updateZone(zone: TemperatureZoneEntity)

    @Delete
    suspend fun deleteZone(zone: TemperatureZoneEntity)

    @Query("DELETE FROM temperature_zones WHERE id = :id")
    suspend fun deleteZoneById(id: Long)

    @Query("SELECT COUNT(*) FROM temperature_zones")
    suspend fun getZoneCount(): Int
}

@Dao
interface UserPreferenceDao {
    @Query("SELECT * FROM user_preferences WHERE profileName = :profileName LIMIT 1")
    fun getPreferenceForProfile(profileName: String): Flow<UserPreferenceEntity?>

    @Query("SELECT * FROM user_preferences LIMIT 1")
    fun getDefaultPreference(): Flow<UserPreferenceEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePreference(preference: UserPreferenceEntity)
}



