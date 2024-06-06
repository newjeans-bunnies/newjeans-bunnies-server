package newjeans.bunnies.newjeansbunnies.domain.report

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "post_report")
data class PostReportEntity(
    @Id
    @Column(nullable = false, unique = true)
    val id: String,
    @Column(nullable = false)
    val nickname: String,
    @Column(nullable = false)
    val postId: String,
    @Column(nullable = false)
    val body: String

)