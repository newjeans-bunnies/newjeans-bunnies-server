package newjeans.bunnies.newjeansbunnies.domain.image

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "image")
data class ImageEntity(
    @Id
    val uuid: String,
    @Column(nullable = false)
    val createDate: String,
    @Column(nullable = false, unique = true)
    val imageURL: String
)