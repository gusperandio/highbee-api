package br.com.highbee.highbee_api.user

import br.com.highbee.highbee_api.role.RoleRepository
import br.com.highbee.highbee_api.config.Crypt
import br.com.highbee.highbee_api.config.Jwt
import br.com.highbee.highbee_api.user.response.LoginResponse
import br.com.highbee.highbee_api.user.response.UserResponse
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    val jwt: Jwt
) {
    companion object {
        val log = LoggerFactory.getLogger(UserService::class.java)
        private val crypt = Crypt()
    }

    fun save(user: User): User {
        user.password = crypt.hashPassword(user.password)
        val userSaved = userRepository.save(user)
        addRole(userSaved.id, "USER")
        return userSaved
    }

    fun addRole(id: Long, roleName: String): Boolean {
        val user = userRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("User not found with id $id")

        if (user.roles.any { it.name == roleName }) return false

        val i = roleRepository.findByName(roleName)
            ?: throw IllegalArgumentException("Role with name $roleName not found")

        user.roles.add(i)
        userRepository.save(user)
        return true
    }


    fun login(email: String, password: String): LoginResponse? {
        val user = userRepository.findByEmail(email)

        if (user == null) {
            log.warn("User not found with email $email")
            return null
        }

        if (!crypt.verifyPassword(password, user.password)) {
            log.warn("Invalid Password!!!")
            return null
        }

        log.info("User logged in!")

        return LoginResponse(
            access_token = jwt.createToken(user),
            UserResponse(user)
        )
    }

    fun findByIdOrNull(id: Long) = userRepository.findByIdOrNull(id)
}