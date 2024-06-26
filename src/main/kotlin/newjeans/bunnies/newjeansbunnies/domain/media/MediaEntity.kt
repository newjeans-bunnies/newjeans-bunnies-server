package newjeans.bunnies.newjeansbunnies.domain.media

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "media")
data class MediaEntity(
    @Id
    val id: String,
    @Column(nullable = false)
    val createDate: String,
    @Column(nullable = false, unique = true)
    val mediaId: String,
    @Column(nullable = false)
    val postId: String,
    @Column(nullable = false)
    var state: Boolean = false,
    @Column(nullable = false)
    val nickname: String
)
