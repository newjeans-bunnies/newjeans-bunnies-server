package newjeans.bunnies.newjeansbunnies.domain.auth

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash(value = "certificationNumberEntity", timeToLive = 61)
data class CertificationNumberEntity(
    @Id
    val phoneNumber: String,
    val certificationNumber: String
)
