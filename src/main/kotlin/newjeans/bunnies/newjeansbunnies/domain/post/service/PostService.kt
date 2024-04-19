package newjeans.bunnies.newjeansbunnies.domain.post.service

import jakarta.transaction.Transactional
import newjeans.bunnies.newjeansbunnies.domain.image.service.ImageService
import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.PostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.CreatePostResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.DisabledPostException
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
@Configuration
class PostService(
    private val postRepository: PostRepository,
    private val userService: UserService,
    private val postGoodService: PostGoodService,
    private val imageService: ImageService
) {
    // 게시글 생성
    fun createPost(postRequestDto: PostRequestDto): CreatePostResponseDto {
        val postId = UUID.randomUUID().toString()
        val postCreateDate = LocalDateTime.now().toString()
        val post = PostEntity(
            uuid = postId,
            createDate = postCreateDate,
            good = 0,
            state = true,
            body = postRequestDto.body,
            userId = postRequestDto.userId
        )
        postRepository.save(post)
        return CreatePostResponseDto(postId, postCreateDate)
    }

    // 게시글 가져오기
    fun getPost(date: String, userId: String): List<PostDto> {
        userService.checkExistUserId(userId)
        val listPost = postRepository.findByCreateDateBeforeOrderByCreateDateDesc(
            date, PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createDate"))
        ).orElseThrow {
            throw NotExistPostIdException
        }

        return listPost.map {
            PostDto(
                uuid = it.uuid,
                userId = it.userId,
                body = it.body,
                createDate = it.createDate,
                good = it.good,
                goodState = postGoodService.getPostGoodState(it.uuid, userId)
            )
        }
    }

    // 게시글 비활성화
    @Transactional
    fun deletePost(postId: String): StatusResponseDto {

        if(!checkDeletePost(postId))
            throw DisabledPostException

        // 예전 게시글 정보
        val oldPost = postRepository.findByUuid(postId).orElseThrow {
            throw NotExistPostIdException
        }

        val postImage = imageService.getImageByPostId(postId)

        val newPost = PostEntity(
            uuid = oldPost.uuid,
            createDate = oldPost.createDate,
            good = oldPost.good,
            body = oldPost.body,
            userId = oldPost.userId,
            state = false
        )

        // 게시글 안에 있는 사진 비활성화
        postImage.map { imageData ->
            imageService.disabledImage(imageData.imageId)
        }

        // 게시글 비활성화
        postRepository.save(newPost)

        return StatusResponseDto(204, "게시글이 삭제됨")
    }

    fun checkDeletePost(postId: String): Boolean {
        return postRepository.findByUuid(postId).orElseThrow {
            throw NotExistPostIdException
        }.state
    }

}