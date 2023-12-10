package newjeans.bunnies.newjeansbunnies.domain.report

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "parents_comment_report")
data class ParentsCommentReportEntity(
    @Id
    val uuid: String,
    val id: String,
    val phoneNumber: String,
    val parentsCommentId: String
)