package br.com.highbee.highbee_api.user

import br.com.highbee.highbee_api.user.request.LoginRequest
import br.com.highbee.highbee_api.user.request.UserRequest
import br.com.highbee.highbee_api.user.response.UserResponse
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
    fun insertUser(@RequestBody @Valid userReq: UserRequest): ResponseEntity<UserResponse> {
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
    fun findByIdRoute(@PathVariable id: Long) =
        userService.findByIdOrNull(id)
            ?.let { UserResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @PutMapping("/{id}/roles/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "WebToken")
    fun grantRole(
        @PathVariable id: Long,
        @PathVariable role: String
    ): ResponseEntity<Void> =
        if (userService.addRole(id, role)) ResponseEntity.ok().build()
        else ResponseEntity.noContent().build()
}