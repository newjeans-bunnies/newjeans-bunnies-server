package newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response

import java.time.LocalDateTime


data class PostDto(
    val id: String,
    val userId: String,
    val userImageUrl: String?,
    val body: String,
    val image: List<String>,
    val createDate: LocalDateTime,
    val good: Long,
    val goodState: Boolean?
)