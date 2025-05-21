package br.com.highbee.highbee_api.role

import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository: JpaRepository<Role, Long> {
    fun findByName(name: String): Role?
}