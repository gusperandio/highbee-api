package br.com.highbee.highbee_api.user.response

import br.com.highbee.highbee_api.user.User
import java.time.LocalDateTime

data class UserRolesListResponse(
    val id: Long,
    val name: String,
    val roles: List<String>
) {
    constructor(u: User) : this(
        id = u.id,
        name = u.name,
        roles = u.roles.map { it.name }
    )
}