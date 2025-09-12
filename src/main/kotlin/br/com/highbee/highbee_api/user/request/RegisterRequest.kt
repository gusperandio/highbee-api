package br.com.highbee.highbee_api.user.request

import br.com.highbee.highbee_api.user.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @field:Email
    val email: String?,
    @NotBlank
    val password: String?,
    val social: Boolean,
    val socialUID: String?
) {
    fun toUser() = User(
        email = email!!,
        password = password!!,
        socialAuth = social,
        socialUID = socialUID!!,
    )
}