package newjeans.bunnies.newjeansbunnies.global.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    @Value("\${cors.originPatterns}")
    private val corsOriginPatterns: String
) : WebMvcConfigurer {
    val allowedOrigins = corsOriginPatterns

    override fun addCorsMappings(registry: CorsRegistry) {
        super.addCorsMappings(registry)

        //auth
        registry.addMapping("/api/auth/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("POST","PATCH", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)

        //comment
        registry.addMapping("/api/comment/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("POST","DELETE")
            .allowedHeaders("*")
            .allowCredentials(true)

        //image
        registry.addMapping("/api/image/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("GET")
            .allowedHeaders("*")
            .allowCredentials(true)

        //post
        registry.addMapping("/api/post/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("GET","POST","DELETE")
            .allowedHeaders("*")
            .allowCredentials(true)

        //user
        registry.addMapping("/api/user/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("GET","PATCH","DELETE")
            .allowedHeaders("*")
            .allowCredentials(true)

        //report
        registry.addMapping("/api/report/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("POST")
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}