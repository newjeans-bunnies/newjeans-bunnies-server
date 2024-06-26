package newjeans.bunnies.newjeansbunnies.domain.post.service

import newjeans.bunnies.newjeansbunnies.domain.comment.service.CommentService
import newjeans.bunnies.newjeansbunnies.domain.image.service.ImageService
import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.FixPostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.PostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.InactivePostException
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserService
import newjeans.bunnies.newjeansbunnies.global.error.exception.InvalidRoleException
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@Configuration
class PostService(
    private val postRepository: PostRepository,
    private val userService: UserService,
    private val postGoodService: PostGoodService,
    private val imageService: ImageService,
    private val commentService: CommentService,
) {
    // 게시글 생성
    fun createPost(postRequestDto: PostRequestDto, userId: String): StatusResponseDto {

        imageService.createImage(postRequestDto.imageId, userId, postRequestDto.postId)

        val post = PostEntity(
            id = postRequestDto.postId, goodCounts = 0, state = true, body = postRequestDto.body, userId = userId
        )

        postRepository.save(post)

        return StatusResponseDto(200, "OK")
    }

    // 게시글 가져오기
    fun getPost(pageSize: Int, page: Int, userId: String?): Slice<PostDto> {

        val pageRequest = PageRequest.of(
            page, pageSize, Sort.by(
                Sort.Direction.DESC, "createdDate"
            )
        )

        val listPost = postRepository.findSliceBy(pageRequest).orElseThrow {
            throw NotExistPostIdException
        }

        return listPost.toPostDto(userId)
    }

    // 특정 유저 게시글 가져오기
    fun getUserPost(pageSize: Int, page: Int, userId: String, authorizedUser: String?): Slice<PostDto> {

        val pageRequest = PageRequest.of(
            page, pageSize, Sort.by(
                Sort.Direction.DESC, "createDate"
            )
        )

        val listPost = postRepository.findSliceBy(pageRequest, userId).orElseThrow {
            throw NotExistPostIdException
        }

        return listPost.toPostDto(authorizedUser)
    }

    // 게시글 수정
    fun fixPost(fixPostRequestDto: FixPostRequestDto, userId: String, postId: String): StatusResponseDto {

        val post = postRepository.findByIdOrNull(postId) ?: throw NotExistPostIdException

        if (post.userId != userId) throw InvalidRoleException

        post.body = fixPostRequestDto.body

        postRepository.save(post)

        return StatusResponseDto(200, "OK")
    }

    // 게시글 비활성화
    fun disabledPost(postId: String, userId: String): StatusResponseDto {

        // 게시글 가져오기
        val post = postRepository.findByIdOrNull(postId) ?: throw NotExistPostIdException

        // 게시글 작성자 이름과 삭제할 유저 이름 비교후 다르면 예외처리
        if (post.userId != userId) throw InvalidRoleException

        // 게시글이 비활성화 되어있다면 예외처리
        if (!post.state) throw InactivePostException

        // 게시글에 있는 사진 들고오기
        val postImage = imageService.getImageByPostId(postId)

        // 게시글에 있는 댓글 들고오기
        val postComment = commentService.getCommentByPostId(postId)

        // 게시글 안에 있는 사진 비활성화
        postImage.map { imageData ->
            imageService.disabledImage(imageData.id, userId)
        }

        // 게시글 안에 있는 댓글 비활성화
        postComment.map { commentData ->
            commentService.disabledComment(commentData.id, userId)
        }

        // 게시글 비활성화
        post.state = false

        // 비활성화된 게시글 저장
        postRepository.save(post)

        return StatusResponseDto(204, "게시글이 삭제됨")
    }

    fun Slice<PostEntity>.toPostDto(userId: String?): Slice<PostDto> {
        return this.map {
            var goodState: Boolean? = null
            if (!userId.isNullOrBlank()) {
                goodState = postGoodService.getPostGoodState(it.id, userId)
            }

            PostDto(
                id = it.id,
                userId = it.userId,
                body = it.body,
                createDate = it.createdDate,
                good = it.goodCounts,
                goodState = goodState,
                image = imageService.getImageByPostId(it.id).map {
                    it.imageKey
                },
                userImageUrl = userService.getUserImage(it.userId).imageURL
            )
        }
    }

    fun getPostByUserId(userId: String): List<PostEntity> {
        return postRepository.findByUserId(userId).orElseThrow { throw NotExistUserIdException }
    }
}