package newjeans.bunnies.newjeansbunnies.domain.image

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "post_image")
data class PostImageEntity(
    @Id
    @Column(name = "image-url")
    val imageUrl: String,
    @Column(name = "create_date")
    val createDate: String,
    @Column(name = "post_uuid")
    val postId: String
)
