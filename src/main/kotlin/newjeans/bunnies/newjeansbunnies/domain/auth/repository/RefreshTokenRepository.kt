package newjeans.bunnies.newjeansbunnies.domain.auth.repository

import newjeans.bunnies.newjeansbunnies.domain.auth.RefreshTokenEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface RefreshTokenRepository: CrudRepository<RefreshTokenEntity, String> {
    fun findByToken(token: String): Optional<RefreshTokenEntity>
}