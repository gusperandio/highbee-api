package br.com.highbee.highbee_api.user.response

import br.com.highbee.highbee_api.role.Role
import br.com.highbee.highbee_api.user.User
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val ageDate: LocalDateTime?,
    val avatar: String,
    val country: String,
    val intention: String,
    val socialAuth: Boolean,
    val termsAgree: Boolean,
    val premium: Boolean,
    val premiumTime: LocalDateTime?,
    val role : MutableSet<String>,
    ) {
    constructor(u: User) : this(
        id = u.id,
        name = u.name,
        email = u.email,
        ageDate = u.age,
        avatar = u.avatar,
        country = u.country,
        intention = u.intention,
        socialAuth = u.socialAuth,
        termsAgree = u.termsAgree,
        premium = u.premium,
        premiumTime = u.premiumTime,
        role = u.roles.map { it.name }.toMutableSet()
    )
}