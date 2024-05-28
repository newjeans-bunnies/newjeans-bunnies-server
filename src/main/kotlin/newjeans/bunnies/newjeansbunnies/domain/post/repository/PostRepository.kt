package newjeans.bunnies.newjeansbunnies.domain.post.repository


import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PostRepository: CrudRepository<PostEntity, String> {

    @Query("SELECT post FROM PostEntity post WHERE post.state = true")
    fun findSliceBy(pageable: Pageable): Optional<Slice<PostEntity>>



    @Query("SELECT post FROM PostEntity post WHERE post.state = true AND post.userId = :userId")
    fun findSliceBy(pageable: Pageable, userId: String): Optional<Slice<PostEntity>>

    fun findByUserId(userId: String): Optional<List<PostEntity>>
}