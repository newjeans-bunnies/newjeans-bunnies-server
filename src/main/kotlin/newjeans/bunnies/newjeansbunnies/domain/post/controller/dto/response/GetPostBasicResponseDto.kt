package newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response


data class GetPostBasicResponseDto(
    val uuid: String,
    val userId: String,
    val userImage: String,
    val body: String,
    val createDate: String,
    val good: Long,
    val images: List<String>
)