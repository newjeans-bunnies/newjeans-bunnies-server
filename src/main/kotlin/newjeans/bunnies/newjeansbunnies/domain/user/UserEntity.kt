package newjeans.bunnies.newjeansbunnies.domain.user


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

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
    @Column(unique = true, nullable = true)
    val imageUrl: String?,
    @Column(nullable = false)
    val country: String,
    @Column(nullable = false)
    val authority: String
) {
    fun hashPassword(passwordEncoder: PasswordEncoder) {
        this.password = passwordEncoder.encode(this.password)
    }

}