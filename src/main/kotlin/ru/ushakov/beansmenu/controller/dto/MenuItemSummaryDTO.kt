package ru.ushakov.beansmenu.controller.dto

import ru.ushakov.beansmenu.domain.DrinkSize
import java.math.BigDecimal

data class MenuItemSummaryDTO(
    val id: String,
    val name: String,
    val category: String,
    val price: BigDecimal,
    val imageBase64: String
)

data class MenuItemDetailsDTO(
    val id: String,
    val name: String,
    val imageBase64: String,
    val category: String,
    val description: String,
    val price: Map<DrinkSize, BigDecimal>,
    val calories: Int,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val weight: Int,
    val composition: List<String>,
    val relatedItems: List<MenuItemSummaryDTO>
)