package newjeans.bunnies.newjeansbunnies.domain.children_comment


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "children_comment")
data class ChildrenCommentEntity(
    @Id
    val uuid: String,
    @Column(nullable = false)
    val id: String,
    @Column(nullable = false)
    val createDate: String,
    @Column(nullable = false)
    val body: String,
    @Column(nullable = false)
    val parentCommentId: String,
    @Column(nullable = false)
    val good: Long,
)
