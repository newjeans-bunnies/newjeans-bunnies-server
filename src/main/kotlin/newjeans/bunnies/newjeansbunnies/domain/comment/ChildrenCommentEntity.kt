package newjeans.bunnies.newjeansbunnies.domain.comment


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
    val parent: String,
    val good: Long,
)
