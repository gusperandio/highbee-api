package br.com.highbee.highbee_api.user.request

import jakarta.validation.constraints.Email

data class LoginRequest(
    @field:Email
    val email: String?,
    val password: String?
)