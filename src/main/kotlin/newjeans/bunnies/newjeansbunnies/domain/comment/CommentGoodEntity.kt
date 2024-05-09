package newjeans.bunnies.newjeansbunnies.domain.comment

import jakarta.persistence.*

@Entity
@Table(name = "comment_good")
data class CommentGoodEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false)
    val userId: String,
    @Column(nullable = false)
    val commentId: String
)