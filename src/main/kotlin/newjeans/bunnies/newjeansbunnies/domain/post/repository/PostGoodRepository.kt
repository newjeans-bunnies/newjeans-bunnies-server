package newjeans.bunnies.newjeansbunnies.domain.post.repository


import newjeans.bunnies.newjeansbunnies.domain.post.PostGoodEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PostGoodRepository: CrudRepository<PostGoodEntity, Long> {

    fun existsByUserIdAndPostId(userId: String, postId: String): Boolean

    fun countByPostId(postId: String): Long

    fun deleteByUserIdAndPostId(userId: String, postId: String)

    fun deleteByPostId(postId: String)

    
}