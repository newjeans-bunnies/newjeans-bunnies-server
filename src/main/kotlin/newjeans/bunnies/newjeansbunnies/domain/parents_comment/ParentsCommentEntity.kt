package newjeans.bunnies.newjeansbunnies.domain.parents_comment


import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "parents_comment")
data class ParentsCommentEntity(
    @Id
    val uuid: String,
    val userId: String,
    val body: String,
    val good: Long,
    val postId: String,
    val createDate: String
)
