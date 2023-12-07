package newjeans.bunnies.newjeansbunnies.domain.comment


import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "parents_comment")
data class ParentsCommentEntity(
    @Id
    val uuid: String,
    val id: String,
    val body: String,
    val good: Long,
    val postId: String,
    val createDate: String
)
