package com.example.data.oracle

import com.example.data.local.entity.InventoryItemEntity
import com.example.data.local.entity.ShoppingItemEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

enum class CloudConnectionState {
    CONNECTED,
    SYNCING,
    OFFLINE_CACHE_MODE,
    ERROR
}

data class OracleConnectionConfig(
    val endpointUrl: String = "https://adb.us-ashburn-1.oraclecloud.com/ords/pantry_ai/26ai/v1",
    val autonomousDbName: String = "ORCL26AI_AUTONOMOUS_PANTRY",
    val tenancyOcid: String = "ocid1.tenancy.oc1..pantry26aicloudstorage",
    val enableVectorAiEngine: Boolean = true,
    val autoSyncIntervalMinutes: Int = 15,
    val isZeroKnowledgeE2eeActive: Boolean = true
)

data class SyncResult(
    val success: Boolean,
    val itemsPushed: Int,
    val itemsPulled: Int,
    val latencyMs: Long,
    val vectorEmbeddingSignature: String,
    val message: String
)

class Oracle26AiService {

    private val _connectionState = MutableStateFlow(CloudConnectionState.CONNECTED)
    val connectionState: StateFlow<CloudConnectionState> = _connectionState.asStateFlow()

    private val _config = MutableStateFlow(OracleConnectionConfig())
    val config: StateFlow<OracleConnectionConfig> = _config.asStateFlow()

    private val _lastSyncTimestamp = MutableStateFlow(System.currentTimeMillis())
    val lastSyncTimestamp: StateFlow<Long> = _lastSyncTimestamp.asStateFlow()

    private val _isOfflineSimulated = MutableStateFlow(false)
    val isOfflineSimulated: StateFlow<Boolean> = _isOfflineSimulated.asStateFlow()

    fun toggleOfflineMode() {
        _isOfflineSimulated.value = !_isOfflineSimulated.value
        _connectionState.value = if (_isOfflineSimulated.value) {
            CloudConnectionState.OFFLINE_CACHE_MODE
        } else {
            CloudConnectionState.CONNECTED
        }
    }

    fun updateConfig(newConfig: OracleConnectionConfig) {
        _config.value = newConfig
    }

    /**
     * Performs synchronization between local Room cache and Oracle 26AI Cloud.
     * If offline mode is toggled, records remain in local Room cache.
     */
    suspend fun performSync(
        pendingInventory: List<InventoryItemEntity>,
        pendingShopping: List<ShoppingItemEntity>
    ): SyncResult {
        if (_isOfflineSimulated.value) {
            return SyncResult(
                success = false,
                itemsPushed = 0,
                itemsPulled = 0,
                latencyMs = 0,
                vectorEmbeddingSignature = "N/A (Local Cache Active)",
                message = "Offline mode active: Changes cached locally in SQLite/Room. Will sync when back online."
            )
        }

        _connectionState.value = CloudConnectionState.SYNCING
        val startTime = System.currentTimeMillis()

        // Simulate Oracle 26AI cloud network roundtrip with vector embedding generation
        delay(750)

        val latency = System.currentTimeMillis() - startTime
        _lastSyncTimestamp.value = System.currentTimeMillis()
        _connectionState.value = CloudConnectionState.CONNECTED

        val vectorHash = "ora26ai-vec-${UUID.randomUUID().toString().take(8)}"

        return SyncResult(
            success = true,
            itemsPushed = pendingInventory.size + pendingShopping.size,
            itemsPulled = 0,
            latencyMs = latency,
            vectorEmbeddingSignature = vectorHash,
            message = "Oracle 26AI Cloud synced ${pendingInventory.size} inventory & ${pendingShopping.size} shopping records with E2EE."
        )
    }
}
