package newjeans.bunnies.newjeansbunnies.domain.user.repository

import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface UserRepository: CrudRepository<UserEntity, String> {
    override fun findById(id: String): Optional<UserEntity>
}