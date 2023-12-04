package newjeans.bunnies.newjeansbunnies.domain.image.controller


import newjeans.bunnies.imageserver.domain.controller.dto.response.PreSignedUrlResponseDto
import newjeans.bunnies.newjeansbunnies.domain.image.service.AwsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/aws")
class AwsController(
    private val awsService: AwsService
) {
    @GetMapping("/post")
    fun postPreSignedURL(
        @RequestParam("fileName") fileName: String
    ): PreSignedUrlResponseDto {
        return PreSignedUrlResponseDto(awsService.getPreSignedUrl(fileName,"post-image/"))
    }

    @GetMapping("/user")
    fun userPreSignedURL(
        @RequestParam("fileName") fileName: String
    ): PreSignedUrlResponseDto {
        return PreSignedUrlResponseDto(awsService.getPreSignedUrl(fileName,"user-image/"))
    }

}