package br.com.highbee.highbee_api.utils

import java.util.*

fun generateGuid(limiter: Boolean): String {
    val uuid = UUID.randomUUID()
    return if (limiter) uuid.toString().split("-")[0] else uuid.toString()
}