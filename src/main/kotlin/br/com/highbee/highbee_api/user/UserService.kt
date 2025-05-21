package br.com.highbee.highbee_api.user

import br.com.highbee.highbee_api.role.RoleRepository
import br.com.highbee.highbee_api.security.Crypt
import br.com.highbee.highbee_api.security.Jwt
import br.com.highbee.highbee_api.user.response.LoginResponse
import br.com.highbee.highbee_api.user.response.UserResponse
import org.slf4j.LoggerFactory
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
}