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
    val uuid: String,
    @Column(nullable = false)
    val userId: String,
    @Column(nullable = false)
    val body: String,
    @Column(nullable = false)
    val createDate: String,
    @Column(nullable = false)
    val good: Long,
    @Column(nullable = false)
    val state: Boolean
)
