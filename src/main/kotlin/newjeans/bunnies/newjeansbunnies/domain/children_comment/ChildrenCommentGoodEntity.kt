package newjeans.bunnies.newjeansbunnies.domain.children_comment


import jakarta.persistence.*


@Entity
@Table(name = "children_comment_good")
data class ChildrenCommentGoodEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false)
    val childrenCommentId: String,
    @Column(nullable = false)
    val userId: String
)
