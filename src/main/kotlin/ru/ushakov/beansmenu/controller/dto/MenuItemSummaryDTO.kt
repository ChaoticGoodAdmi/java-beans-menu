package ru.ushakov.beansmenu.controller.dto

import ru.ushakov.beansmenu.domain.DrinkSize
import java.math.BigDecimal

data class MenuItemSummaryDTO(
    val id: String = String(),
    val name: String = String(),
    val category: String = String(),
    val price: BigDecimal = BigDecimal.ZERO,
    val imageBase64: String = String()
)

data class MenuItemDetailsDTO(
    val id: String = String(),
    val name: String = String(),
    val imageBase64: String = String(),
    val category: String = String(),
    val description: String = String(),
    val price: Map<DrinkSize, BigDecimal> = mapOf(),
    val calories: Int = 0,
    val protein: Double = 0.0,
    val fat: Double = 0.0,
    val carbs: Double = 0.0,
    val weight: Int = 0,
    val composition: List<String> = listOf(),
    val relatedItems: List<MenuItemSummaryDTO> = listOf()
)