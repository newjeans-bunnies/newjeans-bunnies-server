package newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request

data class PreSignedUrlCreateRequest(
    val fileType: String,
    val fileName: String,
    val fileSize: String
)
