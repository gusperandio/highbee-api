package br.com.highbee.highbee_api.user

import br.com.highbee.highbee_api.role.RoleRepository
import br.com.highbee.highbee_api.config.Crypt
import br.com.highbee.highbee_api.config.Jwt
import br.com.highbee.highbee_api.exceptions.NotAuthorized
import br.com.highbee.highbee_api.exceptions.RoleNotFoundException
import br.com.highbee.highbee_api.exceptions.UserNotFoundException
import br.com.highbee.highbee_api.user.request.FinalStepRequest
import br.com.highbee.highbee_api.user.response.LoginResponse
import br.com.highbee.highbee_api.user.response.UserResponse
import br.com.highbee.highbee_api.user.response.UserRolesListResponse
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
open class UserService(
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
        addRole(userSaved.id, "USER_FREE")
        return userSaved
    }


    fun addRole(userId: Long, roleName: String) {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException("Usuário com ID $userId não encontrado.")

        val roleExists = user.roles.any { it.name.equals(roleName, ignoreCase = true) }
        if (roleExists) {
            return
        }


        val role = roleRepository.findByName(roleName)
            .orElseThrow { RoleNotFoundException("Role com nome '$roleName' não encontrada.") }

        // 4. Adiciona a role e salva
        user.roles.add(role)
        userRepository.save(user)
    }


    fun login(email: String, password: String): LoginResponse? {
        val user = userRepository.findByEmail(email)

        if (user == null) {
            return null
        }

        if (!crypt.verifyPassword(password, user.password)) {
            return null
        }

        user.lastLogin = LocalDateTime.now()
        userRepository.save(user)
        return LoginResponse(
            access_token = jwt.createToken(user),
            UserResponse(user)
        )
    }

    //@Cacheable("users", key = "#id")
    open fun findByIdOrNull(id: Long) = userRepository.findByIdOrNull(id)

    fun findByRole(role: String):  List<UserRolesListResponse>
        = userRepository.findByRole(role).map { UserRolesListResponse(it) }

    @Transactional
    fun finishUserRegistration(id: Long, request: FinalStepRequest) {
        val authentication = SecurityContextHolder.getContext().authentication
        val authenticatedUserEmail = authentication.name;

        val user = userRepository.findById(id).orElseThrow { UserNotFoundException("Usuário com ID $id não encontrado.") }

        if (user.email != authenticatedUserEmail)
            throw NotAuthorized("Este usuario não tem permissão para completar o cadastro de outro usuário.")

        user.name = request.name
        user.age = request.age
        user.country = request.country
        user.intention = request.intention
        user.termsAgree = request.termsAgree

        userRepository.save(user)
        return;
    }

}