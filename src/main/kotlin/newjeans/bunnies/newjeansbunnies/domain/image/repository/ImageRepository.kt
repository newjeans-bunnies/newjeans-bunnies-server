package newjeans.bunnies.newjeansbunnies.domain.image.repository

import newjeans.bunnies.newjeansbunnies.domain.image.ImageEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ImageRepository : JpaRepository<ImageEntity, String> {

    fun findByPostId(postId: String): Optional<List<ImageEntity>>

    @Query("SELECT image FROM ImageEntity image WHERE image.createDate < :createDate AND image.state = true ORDER BY image.createDate DESC")
    fun findImagesBeforeCreateDateWithStateTrue(
        @Param("createDate") createDate: String,
        pageable: Pageable
    ): Optional<List<ImageEntity>>

}