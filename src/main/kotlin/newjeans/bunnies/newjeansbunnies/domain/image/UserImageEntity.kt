package newjeans.bunnies.newjeansbunnies.domain.image

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user_image")
data class UserImageEntity(
    @Id
    val id: String,
    @Column(name = "image")
    val imageUrl: String
)