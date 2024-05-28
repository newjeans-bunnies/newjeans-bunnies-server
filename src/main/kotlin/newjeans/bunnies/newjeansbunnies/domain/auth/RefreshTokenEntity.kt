package newjeans.bunnies.newjeansbunnies.domain.auth

import jakarta.persistence.*
import newjeans.bunnies.newjeansbunnies.domain.auth.type.Authority

@Entity
@Table(name = "refresh_token")
data class RefreshTokenEntity(
    @Id
    val id: String,
    @Column(nullable = false, unique = true)
    val token: String,
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    val authority: Authority,
    @Column(nullable = false)
    val expirationTime: Long
)