package ru.ushakov.beansmenu.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.ushakov.beansmenu.config.ObjectIdSerializer

@Document(collection = "coffee_shops")
data class CoffeeShop(

    @JsonSerialize(using = ObjectIdSerializer::class)
    @Id val id: ObjectId = ObjectId.get(),
    val name: String,
    val address: String,
    val city: String
)