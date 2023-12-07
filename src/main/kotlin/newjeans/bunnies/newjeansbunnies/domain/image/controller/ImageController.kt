package newjeans.bunnies.newjeansbunnies.domain.image.controller


import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.PostImagePreSignedUrlResponseDto
import newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response.UserImagePreSignedUrlResponseDto
import newjeans.bunnies.newjeansbunnies.domain.image.service.PostImageService
import newjeans.bunnies.newjeansbunnies.domain.image.service.UserImageService

import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/aws/image")
@Configuration
class ImageController(
    private val postImageService: PostImageService,
    private val userImageService: UserImageService
) {
    @GetMapping("/post")
    fun postPreSignedURL(
        @RequestParam("fileName") fileName: String,
        @RequestParam("uuid") uuid: String
    ): PostImagePreSignedUrlResponseDto {
        return postImageService.getPreSignedUrl(fileName, uuid)
    }

    @GetMapping("/user")
    fun userPreSignedURL(
        @RequestParam("fileName") fileName: String,
        @RequestParam("id") id: String
    ): UserImagePreSignedUrlResponseDto {
        return userImageService.getPreSignedUrl(fileName, id)
    }

}