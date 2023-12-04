package newjeans.bunnies.newjeansbunnies.domain.image

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

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
