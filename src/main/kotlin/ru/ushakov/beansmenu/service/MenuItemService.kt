package ru.ushakov.beansmenu.service

import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import ru.ushakov.beansmenu.controller.MenuItemCreateDTO
import ru.ushakov.beansmenu.controller.MenuItemUpdateDTO
import ru.ushakov.beansmenu.controller.dto.MenuItemDetailsDTO
import ru.ushakov.beansmenu.controller.dto.MenuItemSummaryDTO
import ru.ushakov.beansmenu.domain.DrinkSize
import ru.ushakov.beansmenu.domain.MenuItem
import ru.ushakov.beansmenu.repository.MenuItemRepository
import java.math.BigDecimal

@Service
class MenuItemService(
    private val menuItemRepository: MenuItemRepository
) {
    private val log: Logger = LoggerFactory.getLogger(MenuItemService::class.java)

    @Cacheable(
        value = ["menuSummary"],
        key = "#coffeeShopId.toHexString() + ':' + (#category ?: 'ALL')"
    )
    fun getMenuSummaryByCoffeeShop(
        coffeeShopId: ObjectId,
        category: String?
    ): List<MenuItemSummaryDTO> {
        log.info("Get menu summary by coffee-shop {} and category {}", coffeeShopId.toHexString(), category)
        val menuItems = menuItemRepository.findByCoffeeShopIdAndActive(coffeeShopId, true)
        log.info("Found {} items in DB", menuItems.size)
        return menuItems.filter { item -> category?.let { item.category == it } ?: true }
            .map { it.toSummaryDTO() }
    }


    @Cacheable(value = ["menuItemDetail"], key = "#itemId.toHexString()")
    fun getMenuItemDetails(itemId: ObjectId): MenuItemDetailsDTO? {
        log.info("Get menu item detail by id {}", itemId)
        val menuItem = menuItemRepository.findById(itemId).orElse(null) ?: return null
        val relatedItems = menuItemRepository.findAllById(menuItem.relatedItems)
            .map { it.toSummaryDTO() }
        return menuItem.toDetailsDTO(relatedItems)
    }

    fun getInactiveMenuItems(coffeeShopId: ObjectId): List<MenuItemDetailsDTO> {
        return menuItemRepository.findByCoffeeShopIdAndActive(coffeeShopId, false).map { it.toDetailsDTO(listOf())}
    }

    @CacheEvict(value = ["menuSummary"], allEntries = true)
    fun addMenuItem(coffeeShopId: ObjectId, menuItemDTO: MenuItemCreateDTO): String {
        val menuItem = MenuItem(
            coffeeShopId = coffeeShopId,
            name = menuItemDTO.name,
            imageBase64 = menuItemDTO.imageBase64,
            category = menuItemDTO.category,
            description = menuItemDTO.description,
            price = menuItemDTO.price,
            calories = menuItemDTO.calories,
            protein = menuItemDTO.protein,
            fat = menuItemDTO.fat,
            carbs = menuItemDTO.carbs,
            weight = menuItemDTO.weight,
            composition = menuItemDTO.composition,
            relatedItems = menuItemDTO.relatedItems.map { ObjectId(it) }
        )
        val saved = menuItemRepository.save(menuItem)
        return saved.id.toHexString()
    }

    @CacheEvict(value = ["menuSummary"], allEntries = true)
    fun updateMenuItem(coffeeShopId: String, itemId: ObjectId, updateDTO: MenuItemUpdateDTO): MenuItem {
        val existingItem = menuItemRepository.findById(itemId).orElseThrow {
            IllegalArgumentException("Menu item with ID $itemId not found")
        }
        require(existingItem.coffeeShopId.toHexString() == coffeeShopId) { "Rejected updating different coffee-shop menu" }

        val updatedItem = existingItem.copy(
            name = updateDTO.name ?: existingItem.name,
            imageBase64 = updateDTO.imageBase64 ?: existingItem.imageBase64,
            category = updateDTO.category ?: existingItem.category,
            description = updateDTO.description ?: existingItem.description,
            price = updateDTO.price?.let { mergeMaps(existingItem.price, it) } ?: existingItem.price,
            calories = updateDTO.calories ?: existingItem.calories,
            protein = updateDTO.protein ?: existingItem.protein,
            fat = updateDTO.fat ?: existingItem.fat,
            carbs = updateDTO.carbs ?: existingItem.carbs,
            weight = updateDTO.weight ?: existingItem.weight,
            composition = updateDTO.composition ?: existingItem.composition,
            active = updateDTO.active ?: existingItem.active
        )

        return menuItemRepository.save(updatedItem)
    }

    @CacheEvict(value = ["menuSummary"], allEntries = true)
    fun deleteMenuItem(coffeeShopId: String, itemId: ObjectId) {
        val existingItem = menuItemRepository.findById(itemId).orElseThrow {
            IllegalArgumentException("Menu item with ID $itemId not found")
        }
        require(existingItem.coffeeShopId.toHexString() == coffeeShopId) { "Rejected updating different coffee-shop menu" }
        menuItemRepository.delete(existingItem)
    }

    private fun mergeMaps(
        original: Map<DrinkSize, BigDecimal>,
        updates: Map<DrinkSize, BigDecimal>
    ): Map<DrinkSize, BigDecimal> {
        return original.toMutableMap().apply {
            updates.forEach { (key, value) -> this[key] = value }
        }
    }

    private fun MenuItem.toSummaryDTO() = MenuItemSummaryDTO(
        id = this.id.toHexString(),
        name = this.name,
        category = this.category,
        price = this.price[DrinkSize.MEDIUM] ?: BigDecimal.ZERO,
        imageBase64 = this.imageBase64
    )

    private fun MenuItem.toDetailsDTO(relatedItems: List<MenuItemSummaryDTO>) = MenuItemDetailsDTO(
        id = this.id.toHexString(),
        name = this.name,
        imageBase64 = this.imageBase64,
        category = this.category,
        description = this.description,
        price = this.price,
        calories = this.calories,
        protein = this.protein,
        fat = this.fat,
        carbs = this.carbs,
        weight = this.weight,
        composition = this.composition,
        relatedItems = relatedItems
    )
}