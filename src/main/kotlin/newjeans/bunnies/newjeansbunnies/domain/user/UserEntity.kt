package newjeans.bunnies.newjeansbunnies.domain.user


import jakarta.persistence.*
import newjeans.bunnies.newjeansbunnies.domain.auth.type.Authority

import org.springframework.security.crypto.password.PasswordEncoder


@Entity
@Table(name = "user")
data class UserEntity(
    @Id
    @Column(unique = true, nullable = false)
    val uuid: String,
    @Column(unique = true, nullable = false)
    val userId: String,
    @Column(nullable = false)
    var password: String,
    @Column(nullable = false)
    val phoneNumber: String,
    @Column(nullable = false)
    val language: String,
    @Column(unique = false, nullable = false)
    val imageUrl: String,
    @Column(nullable = false)
    val country: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val authority: Authority,
    @Column(nullable = false)
    val birth: String
) {
    fun hashPassword(passwordEncoder: PasswordEncoder) {
        this.password = passwordEncoder.encode(this.password)
    }

}