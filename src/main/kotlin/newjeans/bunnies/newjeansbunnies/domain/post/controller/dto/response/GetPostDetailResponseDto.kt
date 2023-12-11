package newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response


data class GetPostDetailResponseDto(
    val uuid: String,
    val userId: String,
    val body: String,
    val good: Long,
    val goodStatus: Boolean,
    val createDate: String,
)