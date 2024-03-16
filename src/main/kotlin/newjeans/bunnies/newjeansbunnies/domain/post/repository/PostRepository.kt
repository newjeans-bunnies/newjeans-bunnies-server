package newjeans.bunnies.newjeansbunnies.domain.post.repository


import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostRepository: CrudRepository<PostEntity, String> {

    fun findByUuid(uuid: String): Optional<PostEntity>
    fun findByCreateDateBeforeOrderByCreateDateDesc(createDate: String, pageable: Pageable): Optional<List<PostEntity>>

    fun findTop10ByUserIdAndCreateDateBefore(userId: String, createDate: String): Optional<List<PostEntity>>

    fun findByUserId(userId: String): Optional<List<PostEntity>>

    fun existsByUuid(postId: String): Boolean

    fun deleteByUserId(userId: String)
}