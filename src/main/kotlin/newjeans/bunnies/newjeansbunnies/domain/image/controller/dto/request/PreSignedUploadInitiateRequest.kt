package newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request

data class PreSignedUploadInitiateRequest(
    val fileType: String,
    val fileSize: Long,
)