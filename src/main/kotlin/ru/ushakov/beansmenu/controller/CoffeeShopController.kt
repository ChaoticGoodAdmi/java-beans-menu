package ru.ushakov.beansmenu.controller

import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*
import ru.ushakov.beansmenu.domain.CoffeeShop
import ru.ushakov.beansmenu.service.CoffeeShopService

@RestController
@RequestMapping("/coffee-shops")
class CoffeeShopController(private val coffeeShopService: CoffeeShopService) {

    @GetMapping
    fun getAll(): List<CoffeeShop> = coffeeShopService.getAll()

    @PostMapping
    fun create(@RequestBody coffeeShopDto: CoffeeShopCreateDTO): CoffeeShop {
        val coffeeShop = CoffeeShop(
            name = coffeeShopDto.name,
            address = coffeeShopDto.address,
            city = coffeeShopDto.city
        )
        return coffeeShopService.create(coffeeShop)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = coffeeShopService.delete(ObjectId(id))
}

data class CoffeeShopCreateDTO(
    val name: String = "",
    val address: String = "",
    val city: String = ""
)