package br.com.highbee.highbee_api.strain

import jakarta.persistence.*

@Entity
@Table(name = "strains")
data class Strain(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val nome: String,

    val desc: String,

    val photo: String,

    val rendIndoor: Double,

    val rendOutdoor: Double,

    val thc: Double,

    val automatic: Boolean,

    val photoperiod: Boolean,

    val indica: Boolean,

    val sativa: Boolean,

    val hybrid: Boolean
)
