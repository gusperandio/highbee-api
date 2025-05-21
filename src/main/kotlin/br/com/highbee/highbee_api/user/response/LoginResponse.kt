package br.com.highbee.highbee_api.user.response

data class LoginResponse(
    val access_token: String,
    val user: UserResponse
)