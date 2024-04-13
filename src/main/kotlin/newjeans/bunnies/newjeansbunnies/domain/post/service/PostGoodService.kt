package newjeans.bunnies.newjeansbunnies.domain.post.service


import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.PostGoodEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostGoodResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.DisabledPostException
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Configuration
class PostGoodService(
    private val postRepository: PostRepository,
    private val postGoodRepository: PostGoodRepository,
    private val userService: UserService,
) {
    @Transactional
    fun execute(postId: String, userId: String): PostGoodResponseDto {

        // 유효 PostId인지 확인
        val postData = getValidPost(postId)

        // UserID 대소문자 구분
        userService.checkExistUserId(userId)

        // 게시글 좋아요 상태
        val goodStatus = postGoodRepository.existsByUserIdAndPostId(userId, postId)

        //  게시글 좋아요수
        var goodCount = postData.good

        // 이미 게시글에 좋아요을 눌렀다면
        if (goodStatus) {
            // 좋아요 취소
            postGoodRepository.deleteByUserIdAndPostId(postId = postId, userId = userId)
            // 게시글 좋아요에 -1하기
            --goodCount
        } else {
            // 좋아요
            postGoodRepository.save(PostGoodEntity(postId = postId, userId = userId))
            // 게시글 좋아요에 +1하기
            ++goodCount
        }

        // 게시글 좋아요수 업데이트
        postRepository.save(
            PostEntity(
                uuid = postData.uuid,
                userId = postData.userId,
                body = postData.body,
                createDate = postData.createDate,
                good = goodCount,
                state = postData.state
            )
        )

        // 게시글Id 좋아요 상태, 좋아요수 반환
        return PostGoodResponseDto(
            postId = postData.uuid,
            goodCounts = goodCount,
            goodStatus = !goodStatus
        )
    }

    fun getPostGoodState(postId: String, userId: String): Boolean{
        return postGoodRepository.existsByUserIdAndPostId(userId, postId)
    }

    // 게시글Id 상태 체크
    fun getValidPost(postId: String): PostEntity {
        val post = postRepository.findByUuid(postId).orElseThrow {
            throw NotExistPostIdException
        }
        // 게시글이 비활성화 상태라면 애러 처리
        if (!post.state) throw DisabledPostException

        return post
    }

}