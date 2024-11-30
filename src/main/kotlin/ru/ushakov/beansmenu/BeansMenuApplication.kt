package ru.ushakov.beansmenu

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class BeansMenuApplication

fun main(args: Array<String>) {
	runApplication<BeansMenuApplication>(*args)
}
