package newjeans.bunnies.newjeansbunnies.domain.image

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "image")
data class ImageEntity(
    @Id
    val imageId: String,
    @Column(nullable = false)
    val createDate: String,
    @Column(nullable = false, unique = true)
    val imageKey: String,
    @Column(nullable = false)
    val postId: String,
    @Column(nullable = false)
    var state: Boolean = false,
    @Column(nullable = false)
    val userId: String
)
