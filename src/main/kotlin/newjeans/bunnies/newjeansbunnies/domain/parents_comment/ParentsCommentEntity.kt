package newjeans.bunnies.newjeansbunnies.domain.parents_comment


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "parents_comment")
data class ParentsCommentEntity(
    @Id
    @Column(nullable = false, unique = true)
    val uuid: String,
    @Column(nullable = false)
    val userId: String,
    @Column(nullable = false)
    val body: String,
    @Column(nullable = false)
    val good: Long,
    @Column(nullable = false)
    val postId: String,
    @Column(nullable = false)
    val createDate: String
)
