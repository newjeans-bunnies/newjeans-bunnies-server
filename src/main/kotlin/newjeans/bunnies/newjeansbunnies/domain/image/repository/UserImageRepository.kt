package newjeans.bunnies.newjeansbunnies.domain.image.repository

import newjeans.bunnies.imageserver.domain.Entity.UserImageEntity
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface UserImageRepository: CrudRepository<UserImageEntity, UUID> {
    
}