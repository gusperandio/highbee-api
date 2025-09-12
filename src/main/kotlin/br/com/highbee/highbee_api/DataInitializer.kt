package br.com.highbee.highbee_api


import br.com.highbee.highbee_api.role.Role
import br.com.highbee.highbee_api.role.RoleRepository
import org.springframework.stereotype.Component
import org.springframework.boot.CommandLineRunner

@Component
class DataInitializer(
    private val roleRepository: RoleRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        println("Iniciando a verificação de roles...")
        createRoleIfNotExists("ADMIN", "System Admin")
        createRoleIfNotExists("USER_FREE", "Free User")
        createRoleIfNotExists("USER_PREMIUM", "Premium User")
        createRoleIfNotExists("USER", "Default user role")
        println("Verificação de roles concluída.")
    }

    private fun createRoleIfNotExists(name: String, description: String) {
        if (roleRepository.findByName(name).isEmpty) {
            roleRepository.save(Role(name = name, description = description))
            println("Role criada: $name")
        }
    }
}