package newjeans.bunnies.newjeansbunnies.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        super.addCorsMappings(registry)
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET","POST","PATCH")
            .allowedHeaders("*")
    }
}