package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.oracle.PredictiveAnalyticsEngine
import com.example.data.security.EncryptionManager
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Pantry Inventory", appName)
  }

  @Test
  fun `end to end encryption and decryption roundtrip`() {
    val plaintext = "Household Groceries: 2x Almond Milk, 1x Olive Oil"
    val ciphertext = EncryptionManager.encrypt(plaintext)
    assertNotEquals(plaintext, ciphertext)
    val decrypted = EncryptionManager.decrypt(ciphertext)
    assertEquals(plaintext, decrypted)
  }

  @Test
  fun `database initialization and seed data verification`() {
    kotlinx.coroutines.runBlocking {
      val context = ApplicationProvider.getApplicationContext<Context>()
      val db = com.example.data.local.AppDatabase.getInstance(context)
      val repo = com.example.data.repository.PantryRepository(db, com.example.data.oracle.Oracle26AiService())
      repo.seedInitialDataIfEmpty()
      
      val count = db.categoryDao().getCategoryCount()
      assertTrue(count > 0)

      val dbFile = context.getDatabasePath("pantry_inventory_db")
      assertTrue(dbFile.exists())
    }
  }
}

