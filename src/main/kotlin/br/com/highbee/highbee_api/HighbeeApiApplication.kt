package br.com.highbee.highbee_api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HighbeeApiApplication

fun main(args: Array<String>) {
	runApplication<HighbeeApiApplication>(*args)
}