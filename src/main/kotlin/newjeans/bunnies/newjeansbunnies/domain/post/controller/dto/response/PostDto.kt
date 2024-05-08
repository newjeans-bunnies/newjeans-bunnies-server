package newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response



data class PostDto(
    val uuid: String,
    val userId: String,
    val userImage: String,
    val body: String,
    val image: List<String>,
    val createDate: String,
    val good: Long,
    val goodState: Boolean?
)