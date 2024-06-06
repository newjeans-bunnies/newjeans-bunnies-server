package newjeans.bunnies.newjeansbunnies.domain.user

import jakarta.persistence.*
import newjeans.bunnies.newjeansbunnies.domain.auth.type.Authority
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
@Table(name = "user")
data class UserEntity(
    @Id
    @Column(unique = true, nullable = false)
    val id: String,
    @Column(unique = true, nullable = false)
    var nickname: String,
    @Column(nullable = false)
    var password: String,
    @Column(nullable = false)
    val phoneNumber: String,
    @Column(nullable = false)
    var language: String,
    @Column(unique = false, nullable = true)
    val imageUrl: String? = null,
    @Column(nullable = false)
    var country: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val authority: Authority,
    @Column(nullable = false)
    var birth: String,
    @Column(nullable = false)
    var state: Boolean = true
) {
    fun hashPassword(passwordEncoder: PasswordEncoder) {
        this.password = passwordEncoder.encode(this.password)
    }

}