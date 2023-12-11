package newjeans.bunnies.newjeansbunnies.global.security.config


import com.fasterxml.jackson.databind.ObjectMapper

import newjeans.bunnies.newjeansbunnies.domain.auth.type.Authority
import newjeans.bunnies.newjeansbunnies.global.config.FilterConfig
import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtParser

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtParser: JwtParser,
    private val objectMapper: ObjectMapper,
    private val accessDeniedHandler: AccessDeniedHandler,
    private val authenticationEntryPoint: AuthenticationEntryPoint
) {


    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
            .authorizeHttpRequests { authorize ->

                //auth
                authorize.requestMatchers("/api/auth/**").permitAll()


                //post
                authorize.requestMatchers(HttpMethod.POST, "/api/post/good").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.POST, "/api/post").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.GET, "/api/post/detail/**").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.GET, "/api/post/basic-info/**").permitAll()


                //parentsComment
                authorize.requestMatchers(HttpMethod.POST,"/api/comment/parents/send").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.GET,"/api/comment/parents/detail").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.GET,"/api/comment/parents/basic-info").permitAll()
                authorize.requestMatchers(HttpMethod.DELETE,"/api/comment/parents").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)


                //childrenComment
                authorize.requestMatchers(HttpMethod.POST,"/api/comment/children/send").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.GET,"/api/comment/children/detail").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.GET,"/api/comment/children/basic-info").permitAll()
                authorize.requestMatchers(HttpMethod.DELETE,"/api/comment/children").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)


                //image
                authorize.requestMatchers("/api/aws/image/post").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers("/api/aws/image/user").permitAll()


                //good
                authorize.requestMatchers("/api/post/good").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers("/api/comment/children/good").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers("/api/comment/parents/good").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)


                //user
                authorize.requestMatchers(HttpMethod.GET,"/api/user/get-detail/**").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.GET,"api/user/get-basic/**").permitAll()
                authorize.requestMatchers(HttpMethod.PATCH,"/api/user/update").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.DELETE,"/api/user/delete").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)


                //report
                authorize.requestMatchers(HttpMethod.POST,"/api/report/**").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)

            }
            .exceptionHandling { exceptionHandling ->
                exceptionHandling
                    .accessDeniedHandler(accessDeniedHandler)
                    .authenticationEntryPoint(authenticationEntryPoint)
            }

            .apply(FilterConfig(jwtParser, objectMapper))

        return http.build()
    }

    @Bean
    protected fun passwordEncoder() = BCryptPasswordEncoder()
}