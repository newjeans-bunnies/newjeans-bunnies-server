package newjeans.bunnies.newjeansbunnies.domain.parents_comment


import jakarta.persistence.*


@Entity
@Table(name = "parents_comment_good")
data class ParentsCommentGoodEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    val id: Long = 0,
    @Column(nullable = false)
    val parentsCommentId: String,
    @Column(nullable = false)
    val userId: String
)
