package newjeans.bunnies.newjeansbunnies.domain.user.repository


import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import org.springframework.data.repository.CrudRepository
import java.util.*


interface UserRepository: CrudRepository<UserEntity, String> {

    fun findByUuid(uuid: String): Optional<UserEntity>

    fun findByUserId(userId: String): Optional<UserEntity>

    fun findByUserIdAndPhoneNumber(userId: String, phoneNumber: String): Optional<UserEntity>

    fun findByPhoneNumber(phoneNumber: String): Optional<UserEntity>

    fun deleteByUserId(userId: String)

    fun existsByImageUrl(imageUrl: String): Boolean

    fun existsByUserId(userId: String): Boolean
}