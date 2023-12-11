package newjeans.bunnies.newjeansbunnies.domain.parents_comment


import jakarta.persistence.*


@Entity
@Table(name = "parents_comment_good")
data class ParentsCommentGoodEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val parentsCommentId: String,
    val userId: String
)
