package newjeans.bunnies.newjeansbunnies.domain.comment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "comment")
data class CommentEntity(
    @Id
    @Column(nullable = false, unique = true)
    val uuid: String,
    @Column(nullable = false)
    val userId: String,
    @Column(nullable = false)
    val postId: String,
    @Column(nullable = false)
    var body: String,
    @Column(nullable = false)
    val createDate: String,
    @Column(nullable = false)
    val good: Long,
    @Column(nullable = false)
    var state: Boolean
)