package newjeans.bunnies.newjeansbunnies.domain.user

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
@Table(name = "user")
data class UserEntity(
    @Id
    val id: String,
    var password: String,
    val phoneNumber: String,
    val imageUrl: String?,
    val authority: String
){
    fun hashPassword(passwordEncoder: PasswordEncoder){
        this.password = passwordEncoder.encode(this.password)
    }

}