package newjeans.bunnies.newjeansbunnies.domain.post.repository


import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import java.util.*


interface PostRepository: CrudRepository<PostEntity, String> {
    fun findByCreateDateBeforeOrderByCreateDateDesc(createDate: String, pageable: Pageable): Optional<List<PostEntity>>

    fun findTop10ByUserIdAndCreateDateBefore(userId: String, createDate: String): Optional<List<PostEntity>>

    fun findByUserId(userId: String): Optional<List<PostEntity>>

    fun existsByUuid(postId: String): Boolean

    fun deleteByUserId(userId: String)
}