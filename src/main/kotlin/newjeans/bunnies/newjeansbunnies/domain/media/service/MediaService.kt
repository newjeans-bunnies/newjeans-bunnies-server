package newjeans.bunnies.newjeansbunnies.domain.media.service

import newjeans.bunnies.newjeansbunnies.domain.media.repository.MediaRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
@Configuration
class MediaService(
    private val mediaRepository: MediaRepository,
) {
//    fun getMedia(size: Int, page: Int): Slice<GetMediaRequestDto> {
//
//    }
}