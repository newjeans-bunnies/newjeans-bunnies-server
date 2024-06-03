package newjeans.bunnies.newjeansbunnies.domain.comment.service

import newjeans.bunnies.newjeansbunnies.domain.comment.CommentEntity
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.CommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.FixCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.CommentResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.CreateCommentResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.FixCommentResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.error.exception.NotFoundCommentException
import newjeans.bunnies.newjeansbunnies.domain.comment.repository.CommentRepository
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.InactivePostException
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserService
import newjeans.bunnies.newjeansbunnies.global.error.exception.InvalidRoleException
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
    private val postRepository: PostRepository,
    private val userService: UserService,
    private val commentGoodService: CommentGoodService,
) {

    // 댓글 생성
    fun createComment(commentRequestDto: CommentRequestDto, authorizedUser: String?): CreateCommentResponseDto {

        if(authorizedUser != commentRequestDto.userId) throw InvalidRoleException

        // 유저 확인
        userService.checkExistUserId(commentRequestDto.userId)

        // 유저 활성화 확인
        userService.checkActivationUser(commentRequestDto.userId)

        // 게시글 확인
        checkExistPostId(commentRequestDto.postId)

        // 게시글 활성화 확인
        checkActivationPost(commentRequestDto.postId)

        // 댓글 생성
        val comment = CommentEntity(
            id = UUID.randomUUID().toString(),
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
    fun getComment(postId: String, pageSize: Int, page: Int, authorizedUser: String?): Slice<CommentResponseDto> {

        val pageRequest = PageRequest.of(
            page, pageSize, Sort.by(
                Sort.Direction.DESC, "createDate"
            )
        )

        // 게시글 확인
        checkExistPostId(postId)

        // 게시글 활성화 확인
        checkActivationPost(postId)

        val commentList = commentRepository.findSliceBy(pageRequest, postId).orElseThrow {
            throw NotFoundCommentException
        }

        return commentList.map {

            var goodState: Boolean? = null

            if (!authorizedUser.isNullOrBlank()) {
                goodState = commentGoodService.getCommentGoodState(userId = authorizedUser, commentId = it.id)
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

    // 댓글 수정
    fun fixComment(
        commentId: String, fixCommentRequestDto: FixCommentRequestDto, authorizedUser: String?
    ): FixCommentResponseDto {

        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException

        if (comment.userId != authorizedUser) throw InvalidRoleException

        comment.state = fixCommentRequestDto.state
        comment.body = fixCommentRequestDto.body

        commentRepository.save(comment)

        return FixCommentResponseDto(
            body = comment.body, postId = comment.postId, userId = comment.userId, state = comment.state
        )
    }

    // 댓글 비활성화
    fun disabledComment(commentId: String, authorizedUser: String?): StatusResponseDto {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException

        if (comment.userId != authorizedUser) throw InvalidRoleException

        comment.state = false

        commentGoodService.disabledGoodComment(commentId)

        commentRepository.save(comment)

        return StatusResponseDto(204, "댓글이 비활성화됨")
    }

    // 댓글 활성화
    fun enabledComment(commentId: String, authorizedUser: String?): StatusResponseDto {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException

        if (comment.userId != authorizedUser) throw InvalidRoleException

        comment.state = true

        commentGoodService.enabledGoodComment(commentId)

        commentRepository.save(comment)

        return StatusResponseDto(204, "댓글이 활성화됨")
    }

    fun getCommentByPostId(postId: String): List<CommentEntity> {
        return commentRepository.findByPostId(postId).orElseThrow { throw NotExistPostIdException }
    }

    fun checkExistPostId(postId: String) {
        if (!postRepository.existsById(postId)) throw InactivePostException
    }

    fun checkActivationPost(postId: String) {
        val post = postRepository.findByIdOrNull(postId) ?: throw NotExistPostIdException
        if (!post.state) throw InactivePostException
    }
}