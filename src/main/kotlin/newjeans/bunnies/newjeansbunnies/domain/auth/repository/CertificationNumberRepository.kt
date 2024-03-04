package newjeans.bunnies.newjeansbunnies.domain.auth.repository

import newjeans.bunnies.newjeansbunnies.domain.auth.CertificationNumberEntity
import org.springframework.data.repository.CrudRepository
import java.util.*

interface CertificationNumberRepository: CrudRepository<CertificationNumberEntity, String> {
    fun findByPhoneNumberAndCertificationNumber(phoneNumber: String, certificationNumber: String): Optional<CertificationNumberEntity>
}