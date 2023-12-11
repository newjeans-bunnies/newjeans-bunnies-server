package newjeans.bunnies.newjeansbunnies.domain.report

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "post_report")
data class PostReportEntity(
    @Id
    val uuid: String,
    val userId: String,
    val phoneNumber: String,
    val postId: String
)