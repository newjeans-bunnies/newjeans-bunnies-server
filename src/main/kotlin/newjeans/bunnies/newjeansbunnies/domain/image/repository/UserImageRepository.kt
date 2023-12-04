package newjeans.bunnies.newjeansbunnies.domain.image.repository

import newjeans.bunnies.newjeansbunnies.domain.image.UserImageEntity
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface UserImageRepository: CrudRepository<UserImageEntity, UUID> {
    
}