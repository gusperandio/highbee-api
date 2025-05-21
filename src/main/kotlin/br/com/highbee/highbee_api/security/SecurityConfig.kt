package br.com.highbee.highbee_api.security

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

/**
 * Configura a segurança da API:
 * - Desativa CSRF (por ser uma API REST)
 * - Define a política de sessão como stateless (usamos JWT)
 * - Define filtros CORS globais
 * - Configura regras de acesso por rota, permitindo ou exigindo autenticação
 * - Adiciona o filtro JwtTokenFilter na cadeia de segurança
 *
 * Usamos:
 * - antMatcher() para rotas fixas (ex: GET global)
 * - mvc.pattern() para rotas com parâmetros dinâmicos (ex: /users/{id})
 *
 * A anotação @SecurityScheme configura o Swagger para incluir autenticação JWT via Bearer token.
 */

@Configuration
@EnableMethodSecurity
@SecurityScheme(
    name = "WebToken",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
class SecurityConfig(val jwtTokenFilter: JwtTokenFilter) {
    @Bean
    fun mvc(instrospector: HandlerMappingIntrospector) =
        MvcRequestMatcher.Builder(instrospector)

    @Bean
    fun filterChain(security: HttpSecurity, mvc: MvcRequestMatcher.Builder): DefaultSecurityFilterChain =
        security.cors(Customizer.withDefaults())
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(STATELESS) }
            .exceptionHandling { it.authenticationEntryPoint {_, res, ex ->
                res.sendError(
                    SC_UNAUTHORIZED,
                    if (ex.message.isNullOrEmpty()) "UNAUTHORIZED" else ex.message
                )
            } }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers(antMatcher(HttpMethod.GET)).permitAll()
                    .requestMatchers(mvc.pattern(HttpMethod.GET, "/users/{id}")).authenticated()
                    .requestMatchers(mvc.pattern(HttpMethod.POST, "/users")).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtTokenFilter, BasicAuthenticationFilter::class.java)
            .build()

    @Bean
    fun corsFilter() = CorsConfiguration().apply {
        addAllowedHeader("*")
        addAllowedOrigin("*")
        addAllowedMethod("*")
    }.let {
        UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", it)
        }
    }.let {
        CorsFilter(it)
    }

}