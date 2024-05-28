package newjeans.bunnies.newjeansbunnies.domain.post.repository


import newjeans.bunnies.newjeansbunnies.domain.post.PostGoodEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostGoodRepository: CrudRepository<PostGoodEntity, Long> {

    fun existsByUserIdAndPostId(userId: String, postId: String): Boolean

    fun deleteByUserIdAndPostId(userId: String, postId: String)

    fun findByUserId(userId: String): Optional<List<PostGoodEntity>>

    fun deleteByPostId(postId: String)

}