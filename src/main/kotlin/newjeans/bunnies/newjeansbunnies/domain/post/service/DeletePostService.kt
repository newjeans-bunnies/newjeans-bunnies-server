package newjeans.bunnies.newjeansbunnies.domain.post.service


import jakarta.transaction.Transactional
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.service.DeleteParentsCommentService
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
    private val deleteParentsCommentService: DeleteParentsCommentService,
    private val deletePostImageService: DeletePostImageService
) {

    @Transactional
    fun deletePostByPostId(postId: String): StatusResponseDto {

        postRepository.findById(postId).orElseThrow{
            throw NotExistPostIdException
        }

        //게시글 좋아요 삭제
        postGoodRepository.deleteByPostId(postId)

        //게시글에 달려있는 댓글 삭제
        deleteParentsCommentService.deleteParentsCommentByPostId(postId)
        val postImageDataList = postImageRepository.findByPostId(postId).orElseThrow {
            throw NotExistPostIdException
        }

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
    fun deletePostByUserId(userId: String): StatusResponseDto {
        if (!userRepository.existsByUserId(userId))
            throw NotExistUserIdException

        val postDataList = postRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }

        for (postData in postDataList) {
            deletePostByPostId(postData.uuid)
        }

        return StatusResponseDto(
            status = 204,
            message = "게시글이 삭제됨"
        )
    }

}