package br.com.highbee.highbee_api.user

import br.com.highbee.highbee_api.user.request.LoginRequest
import br.com.highbee.highbee_api.user.request.RegisterRequest
import br.com.highbee.highbee_api.user.response.UserResponse
import br.com.highbee.highbee_api.user.response.UserRolesListResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(val userService: UserService) {

    @PostMapping
    fun insertUser(@RequestBody @Valid userReq: RegisterRequest): ResponseEntity<UserResponse> {
        return userService.save(userReq.toUser())
            .let { userResponse ->
                ResponseEntity.status(HttpStatus.CREATED)
                    .body(UserResponse(userResponse))
            }
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody login: LoginRequest) =
        userService.login(login.email!!, login.password!!)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

    @GetMapping("/{id}")
    @SecurityRequirement(name = "WebToken")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso (pode ser vazia)"),
        ApiResponse(responseCode = "404", description = "Nenhum usuario encontrado com o ID informado"),
    ])
    fun findByIdRoute(@PathVariable id: Long) =
        userService.findByIdOrNull(id)
            ?.let { UserResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()


    @PutMapping("/{id}/roles/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "WebToken")
    fun grantRole(
        @PathVariable id: Long,
        @PathVariable roleName: String
    ): ResponseEntity<Void> {
        userService.addRole(id, roleName)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/by-role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "WebToken")
    fun findUsersByRole(@PathVariable role: String): ResponseEntity<List<UserRolesListResponse>?> =
        userService.findByRole(role.uppercase())
            .let { ResponseEntity.ok(it) }


}