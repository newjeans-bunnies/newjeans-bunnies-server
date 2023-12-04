package newjeans.bunnies.newjeansbunnies.domain.image.repository

import newjeans.bunnies.imageserver.domain.Entity.PostImageEntity
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface PostImageRepository: CrudRepository<PostImageEntity, UUID> {

}