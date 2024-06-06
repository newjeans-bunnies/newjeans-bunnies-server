package newjeans.bunnies.newjeansbunnies.domain.image.controller

import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.ImageResponseDto
import newjeans.bunnies.newjeansbunnies.domain.image.service.ImageService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.global.security.principle.CustomUserDetails
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Slice
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/image")
@Configuration
class ImageController(
    private val imageService: ImageService
) {

    //사진 리스트로 가져오기
    @GetMapping
    fun getImages(
        @RequestParam size: Int, @RequestParam page: Int
    ): Slice<ImageResponseDto> {
        return imageService.getImage(size, page)
    }

    // 특정 사진 가져오기
    @GetMapping("/{nickname}")
    fun getUserImage(
        @RequestParam size: Int, @RequestParam page: Int, @PathVariable nickname: String
    ): Slice<ImageResponseDto> {
        return imageService.getUserImage(size, page, nickname)
    }

    // 사진 비활성화
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping
    fun deleteImage(
        @RequestParam(value = "image-id") imageId: String, @AuthenticationPrincipal auth: CustomUserDetails
    ): StatusResponseDto {
        return imageService.disabledImage(imageId, auth.username)
    }
}