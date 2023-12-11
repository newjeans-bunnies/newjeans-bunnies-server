package newjeans.bunnies.newjeansbunnies.domain.post


import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "post")
data class PostEntity(
    @Id
    val uuid: String,
    val userId: String,
    val body: String,
    val createDate: String,
    val good: Long
)
