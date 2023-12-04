package newjeans.bunnies.imageserver.domain.Entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "post_image")
data class PostImageEntity(
    @Id
    val id: String,
    @Column(name = "create_date")
    val createDate: LocalDateTime,
    @Column(name = "image-url")
    val imageUrl: String,
    @Column(name = "post_uuid")
    val postUUID: String
)
