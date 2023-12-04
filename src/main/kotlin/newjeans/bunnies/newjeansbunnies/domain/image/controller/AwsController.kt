package newjeans.bunnies.newjeansbunnies.domain.image.controller


import newjeans.bunnies.newjeansbunnies.domain.image.dto.response.PreSignedUrlResponseDto
import newjeans.bunnies.newjeansbunnies.domain.image.service.AwsService
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/aws")
@Configuration
class AwsController(
    private val awsService: AwsService
) {
    @GetMapping("/post")
    fun postPreSignedURL(
        @RequestParam("fileName") fileName: String,
        @RequestParam("uuid") uuid: String
    ): PreSignedUrlResponseDto {
        return awsService.getPreSignedUrl(fileName,"post-image/",uuid)
    }

    @GetMapping("/user")
    fun userPreSignedURL(
        @RequestParam("fileName") fileName: String,
        @RequestParam("id") id: String
    ): PreSignedUrlResponseDto {
        return awsService.getPreSignedUrl(fileName,"user-image/",id)
    }

}