package ru.ushakov.beansmenu.controller

import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*
import ru.ushakov.beansmenu.domain.CoffeeShop
import ru.ushakov.beansmenu.service.CoffeeShopService

@RestController
@RequestMapping("/coffee-shops")
class CoffeeShopController(private val coffeeShopService: CoffeeShopService) {

    @GetMapping
    fun getAll(): List<CoffeeShopDto> = coffeeShopService.getAll()

    @PostMapping
    fun create(@RequestBody coffeeShopDto: CoffeeShopCreateDTO): CoffeeShop {
        val coffeeShop = CoffeeShop(
            name = coffeeShopDto.name,
            address = coffeeShopDto.address,
            city = coffeeShopDto.city
        )
        return coffeeShopService.create(coffeeShop)
    }

    @DeleteMapping
    fun delete(@RequestHeader(name = "X-CoffeeShopId", required = true) coffeeShopId: String) {
        check(coffeeShopId.isNotBlank()) { "User is not attached to any coffee-shops" }
        coffeeShopService.delete(ObjectId(coffeeShopId))
    }

}

data class CoffeeShopDto(
    val id: String = String(),
    val name: String = String(),
    val address: String = String(),
    val city: String = String()
)

data class CoffeeShopCreateDTO(
    val name: String = "",
    val address: String = "",
    val city: String = ""
)