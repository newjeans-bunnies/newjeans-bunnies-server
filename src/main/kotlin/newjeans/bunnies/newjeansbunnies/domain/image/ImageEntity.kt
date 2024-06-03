package newjeans.bunnies.newjeansbunnies.domain.image

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name = "image")
data class ImageEntity(
    @Id
    val id: String,
    @CreatedDate
    @Column(updatable = false)
    val createdDate: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = false, unique = true)
    val imageKey: String,
    @Column(nullable = false)
    val postId: String,
    @Column(nullable = false)
    var state: Boolean,
    @Column(nullable = false)
    val userId: String
)
