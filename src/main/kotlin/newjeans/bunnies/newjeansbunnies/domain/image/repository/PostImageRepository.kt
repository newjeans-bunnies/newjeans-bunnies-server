package newjeans.bunnies.newjeansbunnies.domain.image.repository

import newjeans.bunnies.newjeansbunnies.domain.image.PostImageEntity
import org.springframework.data.repository.CrudRepository

interface PostImageRepository: CrudRepository<PostImageEntity, String> {

}