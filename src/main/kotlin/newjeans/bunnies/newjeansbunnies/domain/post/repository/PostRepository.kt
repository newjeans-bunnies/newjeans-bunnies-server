package newjeans.bunnies.newjeansbunnies.domain.post.repository


import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import org.springframework.data.repository.CrudRepository
import java.util.*


interface PostRepository: CrudRepository<PostEntity, String> {
    fun findTop10ByCreateDateBefore(createDate: String): Optional<List<PostEntity>>

    fun findByUserId(userId: String): Optional<List<PostEntity>>

    fun existsByUuid(postId: String): Boolean

    fun deleteByUserId(userId: String)
}