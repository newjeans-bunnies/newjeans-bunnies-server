package newjeans.bunnies.newjeansbunnies.domain.auth


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "refresh_token")
data class RefreshTokenEntity(
    @Id
    val uuid: String,
    @Column(nullable = false, unique = true)
    val token: String,
    @Column(nullable = false)
    val authority: String,
    @Column(nullable = false)
    val expirationTime: Long
)