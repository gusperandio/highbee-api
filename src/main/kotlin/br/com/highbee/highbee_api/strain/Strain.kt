package br.com.highbee.highbee_api.strain

import jakarta.persistence.*

@Entity
@Table(name = "strains")
data class Strain(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,

    val description: String,

    val photo: String,

    val rendIndoor: Double,

    val rendOutdoor: Double,

    val thcPercent: Double,

    val automatic: String,

    val photoperiod: String,

    val indica: String,

    val sativa: String,

    val hybrid: String
)
