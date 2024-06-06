package newjeans.bunnies.newjeansbunnies.domain.user.repository


import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository: CrudRepository<UserEntity, String> {

    fun findByNickname(userId: String?): Optional<UserEntity>

    fun findByNicknameAndPhoneNumber(userId: String, phoneNumber: String): Optional<UserEntity>

    fun findByPhoneNumber(phoneNumber: String): Optional<UserEntity>

    fun existsByNickname(nickname: String): Boolean

    fun existsByPhoneNumber(phoneNumber: String): Boolean

}