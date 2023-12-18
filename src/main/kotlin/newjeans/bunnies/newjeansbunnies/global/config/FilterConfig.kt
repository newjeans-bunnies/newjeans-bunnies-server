package newjeans.bunnies.newjeansbunnies.global.config


import com.fasterxml.jackson.databind.ObjectMapper
import newjeans.bunnies.newjeansbunnies.global.filter.GlobalExceptionFilter
import newjeans.bunnies.newjeansbunnies.global.filter.JwtFilter
import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtParser

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component


@Component
class FilterConfig(
    private val jwtParser: JwtParser,
    private val objectMapper: ObjectMapper
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(builder: HttpSecurity) {
        builder.addFilterBefore(JwtFilter(jwtParser), UsernamePasswordAuthenticationFilter::class.java)
        builder.addFilterBefore(GlobalExceptionFilter(objectMapper), JwtFilter::class.java)
    }
}