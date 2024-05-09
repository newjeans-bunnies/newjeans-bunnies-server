package newjeans.bunnies.newjeansbunnies.domain.post.repository


import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostRepository: CrudRepository<PostEntity, String> {

    fun findByUuid(uuid: String): Optional<PostEntity>

    @Query("SELECT post FROM PostEntity post WHERE post.state = true")
    fun findSliceBy(pageable: Pageable): Optional<List<PostEntity>>

}