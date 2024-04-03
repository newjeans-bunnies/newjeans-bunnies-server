package newjeans.bunnies.newjeansbunnies.domain.image.controller

import newjeans.bunnies.newjeansbunnies.domain.image.service.ImageService
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/image")
@Configuration
class ImageController(
    private val imageService: ImageService
)