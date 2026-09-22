package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.AlertDao
import com.example.data.local.dao.CategoryDao
import com.example.data.local.dao.FamilyMemberDao
import com.example.data.local.dao.InventoryDao
import com.example.data.local.dao.LocationDao
import com.example.data.local.dao.ShoppingDao
import com.example.data.local.dao.SyncLogDao
import com.example.data.local.dao.TemperatureZoneDao
import com.example.data.local.dao.UserPreferenceDao
import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.FamilyMemberEntity
import com.example.data.local.entity.InventoryItemEntity
import com.example.data.local.entity.LocationEntity
import com.example.data.local.entity.OracleSyncLogEntity
import com.example.data.local.entity.RestockAlertEntity
import com.example.data.local.entity.ShoppingItemEntity
import com.example.data.local.entity.TemperatureZoneEntity
import com.example.data.local.entity.UserPreferenceEntity

@Database(
    entities = [
        InventoryItemEntity::class,
        ShoppingItemEntity::class,
        RestockAlertEntity::class,
        OracleSyncLogEntity::class,
        CategoryEntity::class,
        LocationEntity::class,
        FamilyMemberEntity::class,
        TemperatureZoneEntity::class,
        UserPreferenceEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun inventoryDao(): InventoryDao
    abstract fun shoppingDao(): ShoppingDao
    abstract fun alertDao(): AlertDao
    abstract fun syncLogDao(): SyncLogDao
    abstract fun categoryDao(): CategoryDao
    abstract fun locationDao(): LocationDao
    abstract fun familyMemberDao(): FamilyMemberDao
    abstract fun temperatureZoneDao(): TemperatureZoneDao
    abstract fun userPreferenceDao(): UserPreferenceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pantry_inventory_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
