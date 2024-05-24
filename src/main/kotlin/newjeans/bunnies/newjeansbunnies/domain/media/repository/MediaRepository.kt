package newjeans.bunnies.newjeansbunnies.domain.media.repository

import newjeans.bunnies.newjeansbunnies.domain.media.MediaEntity
import org.springframework.data.repository.CrudRepository

interface MediaRepository: CrudRepository<MediaEntity, String>