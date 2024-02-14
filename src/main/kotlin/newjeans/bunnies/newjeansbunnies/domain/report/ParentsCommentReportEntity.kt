package newjeans.bunnies.newjeansbunnies.domain.report

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "parents_comment_report")
data class ParentsCommentReportEntity(
    @Id
    val uuid: String,
    @Column(nullable = false)
    val userId: String,
    @Column(nullable = false)
    val phoneNumber: String,
    @Column(nullable = false)
    val parentsCommentId: String
)