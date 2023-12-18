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

    val allowedOrigins = corsOriginPatterns.split(",").toTypedArray()

    override fun addCorsMappings(registry: CorsRegistry) {
        super.addCorsMappings(registry)

        //auth
        registry.addMapping("/api/auth/**")
            .allowedOriginPatterns(*allowedOrigins)
            .allowedMethods("POST","PATCH")
            .allowedHeaders("*")

        //comment
        registry.addMapping("/api/comment/**")
            .allowedOriginPatterns(*allowedOrigins)
            .allowedMethods("POST","DELETE")
            .allowedHeaders("*")

        //image
        registry.addMapping("/api/aws/image/**")
            .allowedOriginPatterns(*allowedOrigins)
            .allowedMethods("GET")
            .allowedHeaders("*")

        //post
        registry.addMapping("/api/post/**")
            .allowedOriginPatterns(*allowedOrigins)
            .allowedMethods("GET","POST")
            .allowedHeaders("*")

        //user
        registry.addMapping("/api/user/**")
            .allowedOriginPatterns(*allowedOrigins)
            .allowedMethods("GET","PATCH","DELETE")

        //report
        registry.addMapping("/api/report/**")
            .allowedOriginPatterns(*allowedOrigins)
            .allowedMethods("POST")
    }
}