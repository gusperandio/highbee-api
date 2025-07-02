package br.com.highbee.highbee_api.role

import jakarta.validation.constraints.Pattern

class RoleRequest (
    @Pattern(regexp = "[A-Z][A-Z0-9]+$")
    val name: String,
    val description: String?
){
    fun toRole() = Role(name = name, description = description)
}