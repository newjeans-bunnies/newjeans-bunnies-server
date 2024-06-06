package newjeans.bunnies.newjeansbunnies.domain.post

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name = "post")
data class PostEntity(
    @Id
    @Column(nullable = false, unique = true)
    val id: String,
    @Column(nullable = false)
    val userId: String,
    @Column(nullable = false)
    var body: String,
    @CreatedDate
    @Column(updatable = false)
    val createdDate: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = false)
    var goodCounts: Long,
    @Column(nullable = false)
    var state: Boolean,

    )
