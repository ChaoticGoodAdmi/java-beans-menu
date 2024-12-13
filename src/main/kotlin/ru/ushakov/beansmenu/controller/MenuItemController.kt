package ru.ushakov.beansmenu.controller

import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ushakov.beansmenu.controller.dto.MenuItemDetailsDTO
import ru.ushakov.beansmenu.controller.dto.MenuItemSummaryDTO
import ru.ushakov.beansmenu.domain.DrinkSize
import ru.ushakov.beansmenu.domain.MenuItem
import ru.ushakov.beansmenu.service.MenuItemService
import java.math.BigDecimal
import java.util.*

@RestController
@RequestMapping("/menu")
class MenuItemController(private val menuItemService: MenuItemService) {

    private val logger: Logger = LoggerFactory.getLogger(MenuItemController::class.java)

    @GetMapping
    fun getMenu(
        @RequestHeader(name = "X-CoffeeShopId", required = true) coffeeShopId: String,
        @RequestParam(required = false) category: String?
    ): List<MenuItemSummaryDTO> {
        check(coffeeShopId.isNotBlank()) { "User is not attached to any coffee-shops" }
        return menuItemService.getMenuSummaryByCoffeeShop(
            ObjectId(coffeeShopId),
            category
        )
    }

    @GetMapping("/item/{itemId}")
    fun getMenuItemDetails(@PathVariable itemId: String): MenuItemDetailsDTO? {
        return menuItemService.getMenuItemDetails(ObjectId(itemId))
    }

    @PostMapping("/item")
    fun addMenuItem(
        @RequestHeader(name = "X-CoffeeShopId", required = true) coffeeShopId: String,
        @RequestBody menuItemCreateDTO: MenuItemCreateDTO
    ): MenuItem {
        check(coffeeShopId.isNotBlank()) { "User is not attached to any coffee-shops" }
        return menuItemService.addMenuItem(ObjectId(coffeeShopId), menuItemCreateDTO)
    }

    @DeleteMapping("/item/{itemId}")
    fun deleteMenuItem(
        @PathVariable itemId: String
    ): ResponseEntity<Unit> {
        menuItemService.deleteMenuItem(ObjectId(itemId))
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/item/{itemId}")
    fun updateMenuItem(
        @PathVariable itemId: String,
        @RequestBody menuItemUpdateDTO: MenuItemUpdateDTO
    ): MenuItem {
        return menuItemService.updateMenuItem(ObjectId(itemId), menuItemUpdateDTO)
    }

    @GetMapping("/stop-list")
    fun getInactiveMenuItems(@RequestHeader(name = "X-CoffeeShopId", required = true) coffeeShopId: String): List<MenuItem> {
        check(coffeeShopId.isNotBlank()) { "User is not attached to any coffee-shops" }
        return menuItemService.getInactiveMenuItems(ObjectId(coffeeShopId))
    }
}

data class MenuItemCreateDTO(
    val name: String = String(),
    val imageBase64: String = String(),
    val category: String = String(),
    val description: String = String(),
    val price: Map<DrinkSize, BigDecimal> = EnumMap(DrinkSize::class.java),
    val calories: Int = 0,
    val protein: Double = 0.0,
    val fat: Double = 0.0,
    val carbs: Double = 0.0,
    val weight: Int = 0,
    val composition: List<String> = listOf(),
    val relatedItems: List<String> = listOf()
)

data class MenuItemUpdateDTO(
    val name: String? = null,
    val imageBase64: String? = null,
    val category: String? = null,
    val description: String? = null,
    val price: Map<DrinkSize, BigDecimal>? = null,
    val calories: Int? = null,
    val protein: Double? = null,
    val fat: Double? = null,
    val carbs: Double? = null,
    val weight: Int? = null,
    val composition: List<String>? = null,
    val active: Boolean? = null
)