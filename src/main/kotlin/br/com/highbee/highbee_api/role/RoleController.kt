package br.com.highbee.highbee_api.role

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RequestMapping("/roles")
@RestController
class RoleController(val service: RoleService) {

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "WebToken")
    @PostMapping
    fun insert(@Valid @RequestBody role: RoleRequest) =
        service.insert(role.toRole())
            .let { ResponseEntity.ok(it) }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "WebToken")
    @GetMapping
    fun list() = service.findAll()
        .let { ResponseEntity.ok(it) }


}