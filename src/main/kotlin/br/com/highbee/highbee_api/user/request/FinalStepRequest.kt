package br.com.highbee.highbee_api.user.request

import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class FinalStepRequest(
    @field:AssertTrue(message = "É necessário aceitar os termos de uso")
    val termsAgree: Boolean = false,

    @field:NotBlank(message = "Nome é obrigatório")
    val name: String,

    @field:Min(0, message = "Idade deve ser um número positivo")
    val age: LocalDateTime,

    @field:NotBlank(message = "País é obrigatório")
    val country: String,

    @field:NotBlank(message = "Intenção é obrigatória")
    val intention: String
)