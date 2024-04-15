package newjeans.bunnies.newjeansbunnies.domain.image.repository

import newjeans.bunnies.newjeansbunnies.domain.image.ImageEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ImageRepository : CrudRepository<ImageEntity, String> {

    fun findByPostId(postId: String): Optional<List<ImageEntity>>
    fun findByCreateDateBeforeOrderByCreateDateDesc(createDate: String, pageable: Pageable): Optional<List<ImageEntity>>

}