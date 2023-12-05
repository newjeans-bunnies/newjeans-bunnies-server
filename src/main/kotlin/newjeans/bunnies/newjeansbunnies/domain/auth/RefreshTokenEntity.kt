package newjeans.bunnies.newjeansbunnies.domain.auth

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "refresh_token")
data class RefreshTokenEntity(
    @Id
    val id: String,
    val token: String,
    val authority: String,
    val expirationTime: Int
)