package br.com.highbee.highbee_api.news

import br.com.highbee.highbee_api.reports.Report
import br.com.highbee.highbee_api.tags.Tag
import br.com.highbee.highbee_api.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "news")
data class News(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val title: String,

    @Lob
    val argument: String,

    val userId: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    val user: User? = null,

    val important: Boolean = false,

    val minReads: Int? = null,

    val aiValidate: Boolean = false,

    @Lob
    val cape: String,

    @Lob
    val photo1: String? = null,

    @Lob
    val photo1desc: String? = null,

    @Lob
    val photo2: String? = null,

    @Lob
    val photo2desc: String? = null,

    @Lob
    val photo3: String? = null,

    @Lob
    val photo3desc: String? = null,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "news", cascade = [CascadeType.ALL], orphanRemoval = true)
    val reports: List<Report> = emptyList(),

    @ManyToMany
    @JoinTable(
        name = "news_tags",
        joinColumns = [JoinColumn(name = "news_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    val tags: List<Tag> = emptyList()
)