package newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request

data class PreSignedUrlCreateRequest(
    val uploadId: String,
    val partNumber: Int,
)