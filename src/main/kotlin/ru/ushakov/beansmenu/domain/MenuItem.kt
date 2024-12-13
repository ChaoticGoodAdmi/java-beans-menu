package ru.ushakov.beansmenu.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.math.BigDecimal

@Document(collection = "menu_items")
@JsonIgnoreProperties(ignoreUnknown = true)
data class MenuItem(

    @Id val id: ObjectId = ObjectId.get(),
    val coffeeShopId: ObjectId = ObjectId.get(),
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
    val relatedItems: List<ObjectId> = listOf(),
    val active: Boolean = true
) : Serializable

enum class DrinkSize {
    SMALL, MEDIUM, LARGE
}
