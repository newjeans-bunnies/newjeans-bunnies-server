package newjeans.bunnies.newjeansbunnies.domain.post


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


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
    @Column(nullable = false)
    val createDate: String,
    @Column(nullable = false)
    var goodCounts: Long,
    @Column(nullable = false)
    var state: Boolean
)
