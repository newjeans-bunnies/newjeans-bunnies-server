package newjeans.bunnies.newjeansbunnies.domain.report

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "children_comment_report")
data class ChildrenCommentReportEntity(
    @Id
    val uuid: String,
    val userId: String,
    val phoneNumber: String,
    val childrenCommentId: String
)