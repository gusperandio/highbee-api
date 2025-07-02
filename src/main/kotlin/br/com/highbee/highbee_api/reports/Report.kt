package br.com.highbee.highbee_api.reports

import br.com.highbee.highbee_api.news.News
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "reports", indexes = [Index(name = "idx_news_id", columnList = "newsId")])
data class Report(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val idUserReporter: Int,

    val newsId: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsId", insertable = false, updatable = false)
    val news: News? = null,

    val reason: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val updatedAt: LocalDateTime = LocalDateTime.now()
)
