package newjeans.bunnies.newjeansbunnies.domain.post


import jakarta.persistence.*


@Entity
@Table(name = "post_good")
data class PostGoodEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false)
    val userId: String,
    @Column(nullable = false)
    val postId: String
)
