package newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ImageResponseDto(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createDate: LocalDateTime?,
    val imageUrl: String,
    val userId: String
)
