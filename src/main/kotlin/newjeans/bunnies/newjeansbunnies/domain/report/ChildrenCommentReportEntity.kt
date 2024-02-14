package newjeans.bunnies.newjeansbunnies.domain.report

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "children_comment_report")
data class ChildrenCommentReportEntity(
    @Id
    @Column(unique = true, nullable = false)
    val uuid: String,
    @Column(nullable = false)
    val userId: String,
    @Column(nullable = false)
    val phoneNumber: String,
    @Column(nullable = false)
    val childrenCommentId: String
)