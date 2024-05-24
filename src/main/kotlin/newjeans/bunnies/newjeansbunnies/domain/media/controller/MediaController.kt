package newjeans.bunnies.newjeansbunnies.domain.media.controller

import newjeans.bunnies.newjeansbunnies.domain.media.service.MediaService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/media")
class MediaController(
    private val mediaService: MediaService
) {
//    @GetMapping
//    fun getMedia(
//        @RequestParam size: Int,
//        @RequestParam page: Int
//    ): Slice<GetMediaRequestDto>{
//        return mediaService.getMedia(size, page)
//    }
}