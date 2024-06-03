package newjeans.bunnies.newjeansbunnies.global.config

import jakarta.persistence.EntityListeners
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@EntityListeners(AuditingEntityListener::class)
class JpaConfig