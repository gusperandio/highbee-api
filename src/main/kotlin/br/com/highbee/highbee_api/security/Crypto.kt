package br.com.highbee.highbee_api.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class Crypt {
    fun hashPassword(password: String): String {
        val passwordEncoder = BCryptPasswordEncoder()
        return passwordEncoder.encode(password)
    }

    fun verifyPassword(rawPassword: String, hashedPassword: String): Boolean {
        val passwordEncoder = BCryptPasswordEncoder()
        return passwordEncoder.matches(rawPassword, hashedPassword)
    }
}