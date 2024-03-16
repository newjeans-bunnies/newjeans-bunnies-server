package newjeans.bunnies.newjeansbunnies.domain.post.repository

import newjeans.bunnies.newjeansbunnies.domain.post.PostImageEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostImageRepository: CrudRepository<PostImageEntity, String> {
    fun findByPostId(postId: String): Optional<List<PostImageEntity>>
}