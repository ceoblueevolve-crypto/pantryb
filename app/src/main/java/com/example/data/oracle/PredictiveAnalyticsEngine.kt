package com.example.data.oracle

import com.example.data.local.entity.InventoryItemEntity
import kotlin.math.max
import kotlin.math.roundToInt

data class DepletionForecast(
    val itemId: Long,
    val itemName: String,
    val category: String,
    val currentQuantity: Double,
    val unit: String,
    val daysRemaining: Int,
    val predictedDepletionDateEpoch: Long,
    val confidenceScore: Int, // e.g., 96%
    val recommendation: String,
    val isCritical: Boolean
)

data class WasteRiskItem(
    val itemId: Long,
    val itemName: String,
    val category: String,
    val daysUntilExpiry: Int,
    val riskLevel: String, // "HIGH", "MODERATE", "LOW"
    val suggestedRecipeAction: String
)

data class CategoryConsumptionStat(
    val category: String,
    val itemCount: Int,
    val averageTurnoverDays: Int,
    val weeklyExpenseEstimated: Double
)

data class PredictiveAnalyticsReport(
    val overallHealthScore: Int, // 0-100
    val itemsDepletingSoonCount: Int,
    val itemsExpiringSoonCount: Int,
    val estimatedMonthlySpend: Double,
    val wastePreventionScore: Int,
    val depletionForecasts: List<DepletionForecast>,
    val wasteRiskItems: List<WasteRiskItem>,
    val categoryStats: List<CategoryConsumptionStat>,
    val oracleAiModelVersion: String = "Oracle 26AI Autonomous Vector Engine v26.4"
)

object PredictiveAnalyticsEngine {

    fun calculateDaysRemaining(quantity: Double, dailyRate: Double): Int {
        return if (dailyRate > 0) (quantity / dailyRate).toInt() else 30
    }

    fun generateReport(items: List<InventoryItemEntity>): PredictiveAnalyticsReport {
        val now = System.currentTimeMillis()
        val oneDayMs = 86400000L

        val forecasts = items.map { item ->
            val rate = if (item.dailyConsumptionRate > 0) item.dailyConsumptionRate else 0.25
            val rawDays = if (rate > 0) (item.quantity / rate).toInt() else 30
            val daysRemaining = max(0, rawDays)
            val predictedEpoch = now + (daysRemaining * oneDayMs)
            val isCritical = item.quantity <= item.minThreshold || daysRemaining <= 3

            val confidence = when {
                daysRemaining <= 3 -> 97
                daysRemaining <= 7 -> 92
                else -> 85
            }

            val recommendation = when {
                item.quantity <= 0.0 -> "Restock immediately: Out of stock!"
                item.quantity <= item.minThreshold -> "Low stock threshold reached (${item.quantity} ${item.unit}). Restock recommended."
                daysRemaining <= 3 -> "Predicted depletion in $daysRemaining days. Add to shopping list."
                else -> "Stock level optimal for next $daysRemaining days."
            }

            DepletionForecast(
                itemId = item.id,
                itemName = item.name,
                category = item.category,
                currentQuantity = item.quantity,
                unit = item.unit,
                daysRemaining = daysRemaining,
                predictedDepletionDateEpoch = predictedEpoch,
                confidenceScore = confidence,
                recommendation = recommendation,
                isCritical = isCritical
            )
        }.sortedBy { it.daysRemaining }

        val wasteRisks = items.mapNotNull { item ->
            val daysUntilExpiry = ((item.expirationDateEpoch - now) / oneDayMs).toInt()
            if (daysUntilExpiry in 0..10) {
                val risk = when {
                    daysUntilExpiry <= 2 -> "HIGH"
                    daysUntilExpiry <= 5 -> "MODERATE"
                    else -> "LOW"
                }
                val recipe = when (item.category.lowercase()) {
                    "dairy & eggs", "dairy" -> "Use in homemade quiche, smoothies, or baking."
                    "produce" -> "Prep for stir-fry, fresh salad, or freeze for soups."
                    "meat & seafood" -> "Cook tonight or move to freezer."
                    "bakery" -> "Make French toast, croutons, or freeze slices."
                    else -> "Prioritize consumption before shelf life ends."
                }
                WasteRiskItem(
                    itemId = item.id,
                    itemName = item.name,
                    category = item.category,
                    daysUntilExpiry = daysUntilExpiry,
                    riskLevel = risk,
                    suggestedRecipeAction = recipe
                )
            } else null
        }.sortedBy { it.daysUntilExpiry }

        // Category breakdown
        val groupedCategories = items.groupBy { it.category }
        val categoryStats = groupedCategories.map { (cat, list) ->
            val estWeekly = list.size * 5.75
            val avgDays = (list.map { (it.quantity / max(0.1, it.dailyConsumptionRate)).toInt() }.average()).toInt()
            CategoryConsumptionStat(
                category = cat,
                itemCount = list.size,
                averageTurnoverDays = max(2, avgDays),
                weeklyExpenseEstimated = (estWeekly * 10.0).roundToInt() / 10.0
            )
        }

        val criticalCount = forecasts.count { it.isCritical }
        val expiringCount = wasteRisks.count { it.daysUntilExpiry <= 5 }
        val totalEstMonthlySpend = categoryStats.sumOf { it.weeklyExpenseEstimated * 4.2 }

        val healthScore = max(30, 100 - (criticalCount * 12) - (expiringCount * 8))
        val wastePreventionScore = max(40, 100 - (expiringCount * 14))

        return PredictiveAnalyticsReport(
            overallHealthScore = healthScore,
            itemsDepletingSoonCount = criticalCount,
            itemsExpiringSoonCount = expiringCount,
            estimatedMonthlySpend = (totalEstMonthlySpend * 10.0).roundToInt() / 10.0,
            wastePreventionScore = wastePreventionScore,
            depletionForecasts = forecasts,
            wasteRiskItems = wasteRisks,
            categoryStats = categoryStats
        )
    }
}
