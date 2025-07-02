package br.com.highbee.highbee_api.user.request

import jakarta.validation.constraints.Email

data class RegisterRequest (
    @field:Email
    val email: String,
    val password: String,
    val social: Boolean,
    val uidSocial: String
)
