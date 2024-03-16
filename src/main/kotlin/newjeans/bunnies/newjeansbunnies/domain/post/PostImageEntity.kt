package newjeans.bunnies.newjeansbunnies.domain.post

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "post_image")
data class PostImageEntity(
    @Id
    @Column(name = "image-url", nullable = false, unique = true)
    val imageUrl: String,
    @Column(name = "create_date", nullable = false, unique = false)
    val createDate: String,
    @Column(name = "post_uuid", unique = false)
    val postId: String
)
