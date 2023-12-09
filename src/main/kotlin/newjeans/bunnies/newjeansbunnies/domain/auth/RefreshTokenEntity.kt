package newjeans.bunnies.newjeansbunnies.domain.auth


import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "refresh_token")
data class RefreshTokenEntity(
    @Id
    val uuid: String,
    val token: String,
    val authority: String,
    val expirationTime: Long
)