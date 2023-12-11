package newjeans.bunnies.newjeansbunnies.domain.children_comment


import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "children_comment")
data class ChildrenCommentEntity(
    @Id
    val uuid: String,
    val id: String,
    val createDate: String,
    val body: String,
    val parentCommentId: String,
    val good: Long,
)