package newjeans.bunnies.newjeansbunnies.domain.image.repository

import newjeans.bunnies.newjeansbunnies.domain.image.PostImageEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PostImageRepository: CrudRepository<PostImageEntity, String> {
    fun findByPostId(postId: String): Optional<List<PostImageEntity>>
}