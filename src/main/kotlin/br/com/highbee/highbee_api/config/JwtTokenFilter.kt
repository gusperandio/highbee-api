package br.com.highbee.highbee_api.config

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean

/**
 * Filtro JWT responsável por interceptar cada requisição HTTP e extrair o token JWT do header.
 * Se o token for válido, configura a autenticação no SecurityContextHolder para que o Spring Security
 * reconheça o usuário como autenticado durante toda a requisição.
 *
 * Esse filtro é parte do fluxo de segurança e deve estar registrado na cadeia de filtros do Spring.
 */

@Component
class JwtTokenFilter(private val jwt: Jwt): GenericFilterBean() {
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain){
        val auth = jwt.extract(req as HttpServletRequest)

        if(auth != null) SecurityContextHolder.getContext().authentication = auth
        chain.doFilter(req, res)
    }
}