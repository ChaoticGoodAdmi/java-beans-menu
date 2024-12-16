package ru.ushakov.beansmenu.repository

import org.bson.types.ObjectId
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.mongodb.repository.MongoRepository
import ru.ushakov.beansmenu.domain.CoffeeShop
import ru.ushakov.beansmenu.domain.MenuItem

interface CoffeeShopRepository : MongoRepository<CoffeeShop, ObjectId>

interface MenuItemRepository : MongoRepository<MenuItem, ObjectId> {

    fun findByCoffeeShopIdAndActive(coffeeShopId: ObjectId, active: Boolean): List<MenuItem>
}