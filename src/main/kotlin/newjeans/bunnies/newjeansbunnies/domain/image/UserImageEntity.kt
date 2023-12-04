package newjeans.bunnies.imageserver.domain.Entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "user_image")
data class UserImageEntity(
    @Id
    val id: UUID,
    @Column(name = "image")
    val image: ByteArray
)