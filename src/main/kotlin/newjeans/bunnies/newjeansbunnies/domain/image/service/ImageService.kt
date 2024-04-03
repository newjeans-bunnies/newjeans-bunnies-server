package newjeans.bunnies.newjeansbunnies.domain.image.service

import newjeans.bunnies.newjeansbunnies.domain.image.repository.ImageRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
@Configuration
class ImageService(
    private val imageRepository: ImageRepository
) {
    fun createImage(){

    }
}