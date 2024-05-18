package newjeans.bunnies.newjeansbunnies.domain.comment.service

import newjeans.bunnies.newjeansbunnies.domain.comment.CommentEntity
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.CommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.FixCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.CommentResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.CreateCommentResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.FixCommentResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.error.exception.NotFoundCommentException
import newjeans.bunnies.newjeansbunnies.domain.comment.repository.CommentRepository
import newjeans.bunnies.newjeansbunnies.domain.post.service.PostService
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
@Component
class CommentService(
    private val commentRepository: CommentRepository,
    private val userService: UserService,
    private val postService: PostService,
    private val commentGoodService: CommentGoodService,
) {

    // 댓글 생성
    fun createComment(commentRequestDto: CommentRequestDto): CreateCommentResponseDto {

        // 유저 확인
        userService.checkExistUserId(commentRequestDto.userId)
        // 유저 활성화 확인
        userService.checkActivationUser(commentRequestDto.userId)

        // 게시글 확인
        postService.checkExistPostId(commentRequestDto.postId)
        // 게시글 활성화 확인
        postService.checkActivationPost(commentRequestDto.postId)

        // 댓글 생성
        val comment = CommentEntity(
            uuid = UUID.randomUUID().toString(),
            postId = commentRequestDto.postId,
            userId = commentRequestDto.userId,
            createDate = LocalDateTime.now().toString(),
            body = commentRequestDto.body,
            goodCounts = 0L,
            state = true
        )

        // 댓글 저장
        commentRepository.save(comment)

        return CreateCommentResponseDto(
            body = comment.body, createDate = comment.createDate, postId = comment.postId, userId = comment.userId
        )
    }

    // 댓글 가져오기
    fun getComment(postId: String, pageSize: Int, page: Int, accessToken: String): Slice<CommentResponseDto> {

        var userId: String? = null

        if (accessToken.isNotBlank()) {
            userId = userService.getMyData(accessToken).userId
        }

        val pageRequest = PageRequest.of(
            page, pageSize, Sort.by(
                Sort.Direction.DESC, "createDate"
            )
        )

        // 게시글 확인
        postService.checkExistPostId(postId)

        // 게시글 활성화 확인
        postService.checkActivationPost(postId)

        val commentList = commentRepository.findSliceBy(pageRequest, postId).orElseThrow {
            throw NotFoundCommentException
        }

        return commentList.map {

            var goodState: Boolean? = null
            if (!userId.isNullOrBlank()) {
                goodState = commentGoodService.getCommentGoodState(userId = userId, commentId = it.uuid)
            }

            val userImageUrl = userService.getUserImage(it.userId).imageURL

            CommentResponseDto(
                userId = it.userId,
                body = it.body,
                postId = it.postId,
                createDate = it.createDate,
                userImageUrl = userImageUrl,
                goodCounts = it.goodCounts,
                goodState = goodState
            )
        }
    }

    fun fixComment(commentId: String, fixCommentRequestDto: FixCommentRequestDto, accessToken: String): FixCommentResponseDto {

        var userId: String? = null

        if (accessToken.isNotBlank()) {
            userId = userService.getMyData(accessToken).userId
        }

        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException

        if(comment.userId != userId)
            throw NotFoundCommentException

        comment.state = fixCommentRequestDto.state
        comment.body = fixCommentRequestDto.body

        commentRepository.save(comment)

        return FixCommentResponseDto(
            body = comment.body, postId = comment.postId, userId = comment.userId, state = comment.state
        )
    }


    fun deleteComment(commentId: String, accessToken: String): StatusResponseDto {
        var userId: String? = null

        if (accessToken.isNotBlank()) {
            userId = userService.getMyData(accessToken).userId
        }

        val comment = commentRepository.findById(commentId).orElseThrow {
            throw NotFoundCommentException
        }

        if(comment.userId != userId)
            throw NotFoundCommentException

        comment.state = false

        commentRepository.save(comment)

        return StatusResponseDto(204, "댓글이 비활성화됨")
    }

}