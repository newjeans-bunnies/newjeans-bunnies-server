package newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response

data class PostImagePreSignedUrlResponseDto(
    val preSignedURL: String,
    val imageName: String
)
