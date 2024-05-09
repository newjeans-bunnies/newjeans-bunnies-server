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
            .allowedMethods("POST","PATCH")
            .allowedHeaders("*")

        //comment
        registry.addMapping("/api/comment/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("POST","DELETE")
            .allowedHeaders("*")

        //image
        registry.addMapping("/api/image/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("GET")
            .allowedHeaders("*")

        //post
        registry.addMapping("/api/post/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("GET","POST","DELETE")
            .allowedHeaders("*")

        //user
        registry.addMapping("/api/user/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("GET","PATCH","DELETE")
            .allowedHeaders("*")

        //report
        registry.addMapping("/api/report/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("POST")
            .allowedHeaders("*")
    }
}