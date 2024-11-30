package ru.ushakov.beansmenu.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import ru.ushakov.beansmenu.domain.CoffeeShop
import ru.ushakov.beansmenu.repository.CoffeeShopRepository

@Service
class CoffeeShopService(private val coffeeShopRepository: CoffeeShopRepository) {
    fun getAll(): List<CoffeeShop> = coffeeShopRepository.findAll()
    fun create(coffeeShop: CoffeeShop): CoffeeShop = coffeeShopRepository.save(coffeeShop)
    fun delete(id: ObjectId) = coffeeShopRepository.deleteById(id)
}

