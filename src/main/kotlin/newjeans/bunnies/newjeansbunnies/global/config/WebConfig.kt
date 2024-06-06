package newjeans.bunnies.newjeansbunnies.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    @Value("\${cors.originPatterns}") private val corsOriginPatterns: String
) : WebMvcConfigurer {
    val allowedOrigins = corsOriginPatterns

    override fun addCorsMappings(registry: CorsRegistry) {
        super.addCorsMappings(registry)

        //auth
        registry.addMapping("/api/auth/**").allowedOrigins(allowedOrigins).allowedMethods("POST", "PATCH", "OPTIONS")
            .allowCredentials(true)

        //comment
        registry.addMapping("/api/comment/**").allowedOrigins(allowedOrigins).allowedMethods("POST", "GET", "OPTIONS")
            .allowedHeaders("Authorization").allowCredentials(true)

        //image
        registry.addMapping("/api/image/**").allowedOrigins(allowedOrigins).allowedMethods("GET", "PATCH", "OPTIONS")
            .allowedHeaders("Authorization").allowCredentials(true)

        //post
        registry.addMapping("/api/post/**").allowedOrigins(allowedOrigins)
            .allowedMethods("GET", "POST", "PATCH", "OPTIONS").allowedHeaders("Authorization").allowCredentials(true)

        //user
        registry.addMapping("/api/user/**").allowedOrigins(allowedOrigins).allowedMethods("GET", "PATCH", "OPTIONS")
            .allowedHeaders("Authorization").allowCredentials(true)

        //report
        registry.addMapping("/api/report/**").allowedOrigins(allowedOrigins).allowedMethods("POST", "OPTIONS")
            .allowedHeaders("Authorization").allowCredentials(true)

        //media
        registry.addMapping("/api/media").allowedOrigins(allowedOrigins)
            .allowedMethods("GET", "OPTIONS")
            .allowCredentials(true)
    }
}