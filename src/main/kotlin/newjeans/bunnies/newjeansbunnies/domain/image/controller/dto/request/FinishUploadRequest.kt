package newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.request

data class FinishUploadRequest(
    val uploadId: String,
    val parts: List<Part>,
    val fileFullName: String,
    val postId: String
) {
    data class Part(
        val partNumber: Int,
        val eTag: String,
    )
}