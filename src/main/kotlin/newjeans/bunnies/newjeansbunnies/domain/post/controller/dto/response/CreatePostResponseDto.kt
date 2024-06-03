package newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime


data class CreatePostResponseDto(
    val postId: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createDate: LocalDateTime?,
)