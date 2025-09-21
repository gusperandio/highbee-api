package br.com.highbee.highbee_api.tags

import br.com.highbee.highbee_api.config.Crypt
import br.com.highbee.highbee_api.config.Jwt
import br.com.highbee.highbee_api.role.RoleRepository
import br.com.highbee.highbee_api.user.UserRepository
import br.com.highbee.highbee_api.user.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
open class TagService(
    val tagRepository: TagRepository,
    val jwt: Jwt
) {
    companion object {
        val log = LoggerFactory.getLogger(UserService::class.java)
    }

    fun save(tag: String): Tag =
         tagRepository.save(Tag(name = tag))

    fun findAll(): List<Tag> =
        tagRepository.findAll()

    fun findById(tagId: Long): Tag =
        tagRepository.findById(tagId).orElseThrow { Exception("Tag not found") }

    fun delete(tagId: Long) =
        tagRepository.deleteById(tagId)
}