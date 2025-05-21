package br.com.highbee.highbee_api.user


import br.com.highbee.highbee_api.role.Role
import jakarta.persistence.*
import jakarta.persistence.ManyToMany
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    var email: String = "",

    @NotNull
    var name: String = "",

    @NotNull
    val ageDate: String = "",

    @NotNull
    var password: String = "",

    @NotNull
    val avatar: String = "",

    @NotNull
    val country: String = "",

    @NotNull
    val intention: String = "",

    @NotNull
    val socialAuth: Boolean = false,

    @NotNull
    val termsAgree: Boolean = false,

    @NotNull
    val blackList: Boolean = false,

    @NotNull
    val premium: Boolean = false,

    @Nullable
    val premiumTime: LocalDateTime? = null,

    @Nullable
    val lastLogin: LocalDateTime? = null,

    @NotNull
    val createdAt: LocalDateTime = LocalDateTime.now(),

    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @ManyToMany
    @JoinTable(
        name = "UserRole",
        joinColumns = [JoinColumn(name = "idUser", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "idRole", referencedColumnName = "id")]
    )
    val roles: MutableSet<Role> = mutableSetOf()
)