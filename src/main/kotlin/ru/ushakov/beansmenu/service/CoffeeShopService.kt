package ru.ushakov.beansmenu.service

import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import ru.ushakov.beansmenu.controller.CoffeeShopDto
import ru.ushakov.beansmenu.domain.CoffeeShop
import ru.ushakov.beansmenu.repository.CoffeeShopRepository

@Service
class CoffeeShopService(private val coffeeShopRepository: CoffeeShopRepository) {

    private val logger: Logger = LoggerFactory.getLogger(CoffeeShopService::class.java)

    @Cacheable("coffeeShops")
    fun getAll(): List<CoffeeShopDto> =
        coffeeShopRepository.findAll().map {
        CoffeeShopDto(
            id = it.id.toHexString(),
            name = it.name,
            address = it.address,
            city = it.city
        )
    }

    @CacheEvict(value = ["coffeeShops"], allEntries = true)
    fun create(coffeeShop: CoffeeShop): CoffeeShop = coffeeShopRepository.save(coffeeShop)

    @CacheEvict(value = ["coffeeShops"], allEntries = true)
    fun delete(id: ObjectId) = coffeeShopRepository.deleteById(id)

}

