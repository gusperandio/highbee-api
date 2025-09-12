package br.com.highbee.highbee_api.user


import br.com.highbee.highbee_api.role.Role
import jakarta.persistence.*
import jakarta.persistence.ManyToMany
import jakarta.validation.constraints.Null
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.time.LocalDateTime
@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    var email: String = "",

    var name: String = "",

    val age: LocalDateTime? = null,

    var password: String = "",

    val avatar: String = "",

    val country: String = "",

    val intention: String = "",

    val socialAuth: Boolean = false,

    val socialUID: String? = null,

    val termsAgree: Boolean = false,

    val blackList: Boolean = false,

    val premium: Boolean = false,

    val premiumTime: LocalDateTime? = null,

    val lastLogin: LocalDateTime? = null,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "idUser", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "idRole", referencedColumnName = "id")]
    )
    val roles: MutableSet<Role> = mutableSetOf()
)
