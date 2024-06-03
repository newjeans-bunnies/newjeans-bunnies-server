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

                //preflight
                authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                //auth
                authorize.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll() // 로그인
                authorize.requestMatchers(HttpMethod.POST, "/api/auth/signup").permitAll() // 회원가입
                authorize.requestMatchers(HttpMethod.PATCH, "/api/auth/refresh").permitAll() // 토큰 재발급
                authorize.requestMatchers(HttpMethod.DELETE,"/api/auth").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name) //유저 삭제
                authorize.requestMatchers(HttpMethod.POST, "/api/auth/phonenumber/verify").permitAll() // 전화번호 인증
                authorize.requestMatchers(HttpMethod.POST, "/api/auth/phonenumber").permitAll() // 전화번호 인증

                //post
                authorize.requestMatchers(HttpMethod.POST, "/api/post/good").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name) // 게시글 좋아요
                authorize.requestMatchers(HttpMethod.GET, "/api/post").permitAll() // 게시글 가져오기
                authorize.requestMatchers(HttpMethod.GET, "/api/post/**").permitAll() // 특정 게시글 가져오기
                authorize.requestMatchers(HttpMethod.PATCH, "/api/post/fix").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name) // 게시글 수정
                authorize.requestMatchers(HttpMethod.PATCH, "/api/post/delete").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name) // 게시글 비활성화
                authorize.requestMatchers(HttpMethod.POST, "/api/post").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name) // 게시글 만들기

                //user
                authorize.requestMatchers(HttpMethod.GET,"/api/user/me").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name) // 내 정보 가져오기
                authorize.requestMatchers(HttpMethod.GET,"/api/user/**").permitAll()
                authorize.requestMatchers(HttpMethod.PATCH,"/api/user/fix").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)

                //image
                authorize.requestMatchers(HttpMethod.POST, "/api/image").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name) // 사진 만들기
                authorize.requestMatchers(HttpMethod.PATCH, "/api/image").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name) // 사진 비활성화
                authorize.requestMatchers(HttpMethod.GET, "/api/image").permitAll() // 사진 가져오기
                authorize.requestMatchers(HttpMethod.GET, "/api/image/**").permitAll() // 특정 사진 가져오기

                //report
                authorize.requestMatchers(HttpMethod.POST,"/api/report/**").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)

                //comment
                authorize.requestMatchers(HttpMethod.GET, "/api/comment").permitAll()
                authorize.requestMatchers(HttpMethod.PATCH, "/api/comment/delete").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.PATCH, "/api/comment/fix").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.POST, "/api/comment").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)
                authorize.requestMatchers(HttpMethod.POST, "/api/comment/good").hasAnyAuthority(Authority.USER.name, Authority.MANAGER.name)

                //media
                authorize.requestMatchers(HttpMethod.GET, "/api/media").permitAll()

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