package ru.ushakov.beansmenu.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "coffee_shops")
@JsonIgnoreProperties(ignoreUnknown = true)
data class CoffeeShop(

    @Id val id: ObjectId = ObjectId.get(),
    val name: String = String(),
    val address: String = String(),
    val city: String = String()

)