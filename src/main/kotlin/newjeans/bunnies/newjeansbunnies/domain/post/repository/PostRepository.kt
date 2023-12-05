package newjeans.bunnies.newjeansbunnies.domain.post.repository

import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import org.springframework.data.repository.CrudRepository

interface PostRepository: CrudRepository<PostEntity, String> {

}