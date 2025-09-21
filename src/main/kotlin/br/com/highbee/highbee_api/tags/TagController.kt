package br.com.highbee.highbee_api.tags

import br.com.highbee.highbee_api.user.request.RegisterRequest
import br.com.highbee.highbee_api.user.response.UserResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tags")
class TagController(val tagService: TagService) {

    @GetMapping
    @SecurityRequirement(name = "WebToken")
    fun listTags(): ResponseEntity<List<Tag>> =
        tagService.findAll()
            .let { tagsResponse ->
                ResponseEntity.ok(tagsResponse)
            }

    @GetMapping("/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "WebToken")
    fun getTagById(@PathVariable tagId: Long): ResponseEntity<Tag> =
        tagService.findById(tagId)
            .let { tagResponse ->
                ResponseEntity.ok(tagResponse)
            }

    @PostMapping("/{tagName}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "WebToken")
    fun insertTag(@PathVariable tagName: String): ResponseEntity<Tag> =
        tagService.save(tagName)
            .let { tagResponse ->
                ResponseEntity.status(HttpStatus.CREATED)
                    .body(tagResponse)
            }

    @DeleteMapping("/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "WebToken")
    fun deleteTag(@PathVariable tagId: Long): ResponseEntity<Void> =
        tagService.delete(tagId)
            .let {
        return ResponseEntity.noContent().build() }

}