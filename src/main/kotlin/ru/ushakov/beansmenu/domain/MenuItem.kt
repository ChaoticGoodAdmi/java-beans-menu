package ru.ushakov.beansmenu.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.ushakov.beansmenu.config.ObjectIdSerializer
import java.math.BigDecimal

@Document(collection = "menu_items")
data class MenuItem(

    @JsonSerialize(using = ObjectIdSerializer::class)
    @Id val id: ObjectId = ObjectId.get(),
    val coffeeShopId: ObjectId,
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
    val relatedItems: List<ObjectId> = listOf(),
    val active: Boolean = true
)

enum class DrinkSize {
    SMALL, MEDIUM, LARGE
}
