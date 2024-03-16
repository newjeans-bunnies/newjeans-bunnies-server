package newjeans.bunnies.newjeansbunnies.domain.auth.repository

import newjeans.bunnies.newjeansbunnies.domain.auth.RefreshTokenEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface RefreshTokenRepository: CrudRepository<RefreshTokenEntity, String> {
    fun findByToken(token: String): Optional<RefreshTokenEntity>
}