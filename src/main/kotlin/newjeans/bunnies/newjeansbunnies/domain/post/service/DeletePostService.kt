package newjeans.bunnies.newjeansbunnies.domain.post.service


import jakarta.transaction.Transactional
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.PostImageEntity
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostImageRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Service
@Configuration
class DeletePostService(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val postImageRepository: PostImageRepository,
    private val postGoodRepository: PostGoodRepository,
    private val deletePostImageService: DeletePostImageService
) {

    @Transactional
    suspend fun deletePost(postId: String): StatusResponseDto {

        checkValidPostId(postId)

        //게시글 좋아요 삭제
        postGoodRepository.deleteByPostId(postId)

        //게시글에 달려있는 사진 가져오기
        val postImageDataList = getPostImage(postId)

        //사진 삭제
        for (postImageData in postImageDataList) {
            deletePostImageService.deletePostImage(postImageData.imageUrl)
        }

        //게시글 삭제
        postRepository.deleteById(postId)

        return StatusResponseDto(
            status = 204,
            message = "게시글이 삭제됨"
        )
    }

    @Transactional
    suspend fun deletePostByUserId(userId: String): StatusResponseDto {
        if (!userRepository.existsByUserId(userId))
            throw NotExistUserIdException

        val postDataList = getValidPost(userId)

        for (postData in postDataList) {
            deletePost(postData.uuid)
        }

        return StatusResponseDto(
            status = 204,
            message = "게시글이 삭제됨"
        )
    }

    private suspend fun checkValidPostId(postId: String){
        postRepository.findByUuid(postId).orElseThrow{
            throw NotExistPostIdException
        }
    }

    private suspend fun getValidPost(userId: String): List<PostEntity>{
        return postRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }
    }

    private suspend fun getPostImage(postId: String): List<PostImageEntity>{
        return postImageRepository.findByPostId(postId).orElseThrow {
            throw NotExistPostIdException
        }
    }

}