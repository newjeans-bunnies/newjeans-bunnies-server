package newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response

data class CreatePreSignedUrlResponse(
    val preSignedUrl: String,
    val fileName: String,
    val postId: String,
    val fileSize: String,
    val fileType: String
)
