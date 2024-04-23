package newjeans.bunnies.newjeansbunnies.global.config


import com.fasterxml.jackson.databind.ObjectMapper
import newjeans.bunnies.newjeansbunnies.domain.auth.type.Authority
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
                authorize.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll() // 로그인
                authorize.requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll() // 회원가입
                authorize.requestMatchers(HttpMethod.PATCH, "/api/auth/refresh").permitAll() // 토큰 재발급
                authorize.requestMatchers(HttpMethod.DELETE,"/api/auth").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name) //유저 삭제
                authorize.requestMatchers(HttpMethod.POST, "/api/auth/phonenumber/verify").permitAll() // 전화번호 인증
                authorize.requestMatchers(HttpMethod.POST, "/api/auth/phonenumber").permitAll() // 전화번호 인증

                //post
                authorize.requestMatchers(HttpMethod.POST, "/api/post/good").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.GET, "/api/post").permitAll()
                authorize.requestMatchers(HttpMethod.POST, "/api/post").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.DELETE, "/api/post").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)

                //good
                authorize.requestMatchers("/api/post/good").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers("/api/comment/children/good").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers("/api/comment/parents/good").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)

                //user
                authorize.requestMatchers(HttpMethod.GET,"/api/user/get-detail").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.GET,"api/user/get-basic/**").permitAll()
                authorize.requestMatchers(HttpMethod.PATCH,"/api/user/update").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.GET,"/api/user/check/userid").permitAll()
                authorize.requestMatchers(HttpMethod.GET,"/api/user/check/phonenumber").permitAll()
                authorize.requestMatchers(HttpMethod.GET,"/api/user/support").permitAll()
                authorize.requestMatchers(HttpMethod.GET,"/api/user/image/*").permitAll()

                //image
                authorize.requestMatchers(HttpMethod.POST, "api/image").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.POST, "api/image/complete-upload").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.POST, "api/image/abort-upload").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.DELETE, "api/image").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.GET, "api/image").permitAll()

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