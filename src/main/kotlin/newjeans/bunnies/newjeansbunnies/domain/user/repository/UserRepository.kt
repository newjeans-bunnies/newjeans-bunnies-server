package newjeans.bunnies.newjeansbunnies.domain.user.repository


import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository: CrudRepository<UserEntity, String> {

    fun findByUserId(userId: String?): Optional<UserEntity>

    fun findByUserIdAndPhoneNumber(userId: String, phoneNumber: String): Optional<UserEntity>

    fun findByPhoneNumber(phoneNumber: String): Optional<UserEntity>

    fun deleteByUserId(userId: String)
}