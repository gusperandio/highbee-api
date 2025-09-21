package br.com.highbee.highbee_api.tags

import br.com.highbee.highbee_api.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, Long> {

}