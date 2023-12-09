package newjeans.bunnies.newjeansbunnies.domain.user.repository


import newjeans.bunnies.newjeansbunnies.domain.user.UserEntity
import org.springframework.data.repository.CrudRepository
import java.util.*


interface UserRepository: CrudRepository<UserEntity, String> {

    fun findByUserId(userId: String): Optional<UserEntity>

    fun findByPhoneNumber(phoneNumber: String): Optional<UserEntity>
}