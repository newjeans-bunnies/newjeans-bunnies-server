package newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response


data class GetPostResponseDto(
    val uuid: String,
    val id: String,
    val body: String,
    val createDate: String,
    val good: Long
)