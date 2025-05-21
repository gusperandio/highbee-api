package br.com.highbee.highbee_api.user.request

data class LoginRequest(
    val email: String?,
    val password: String?
)