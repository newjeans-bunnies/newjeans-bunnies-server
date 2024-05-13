package newjeans.bunnies.newjeansbunnies.domain.post.service

import jakarta.transaction.Transactional
import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.image.service.AwsUploadService
import newjeans.bunnies.newjeansbunnies.domain.image.service.ImageService
import newjeans.bunnies.newjeansbunnies.domain.post.PostEntity
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.request.PostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.CreatePostResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.DisabledPostException
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.InactivePostException
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtParser
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@Configuration
class PostService(
    private val postRepository: PostRepository,
    private val userService: UserService,
    private val postGoodService: PostGoodService,
    private val imageService: ImageService,
    private val awsUploadService: AwsUploadService,
    private val jwtParser: JwtParser
) {
    // 게시글 생성
    fun createPost(postRequestDto: PostRequestDto): CreatePostResponseDto {
        val postCreateDate = LocalDateTime.now().toString()
        awsUploadService.completeUpload(postRequestDto.postId)
        val post = PostEntity(
            uuid = postRequestDto.postId,
            createDate = postCreateDate,
            good = 0,
            state = true,
            body = postRequestDto.body,
            userId = postRequestDto.userId
        )
        postRepository.save(post)
        return CreatePostResponseDto(postRequestDto.postId, postCreateDate)
    }

    // 게시글 가져오기
    fun getPost(pageSize: Int, page: Int, accessToken: String): Slice<PostDto> {
        var userId: String? = null

        if (accessToken.isNotBlank()) {
            userId = userService.getUserId(jwtParser.getClaims(accessToken).body["jti"].toString())
        }

        val pageRequest = PageRequest.of(
            page, pageSize, Sort.by(
                Sort.Direction.DESC, "createDate"
            )
        )

        val listPost = postRepository.findSliceBy(pageRequest).orElseThrow {
            throw NotExistPostIdException
        }

        return listPost.map {
            var goodState: Boolean? = null

            if (!userId.isNullOrBlank()) {
                goodState = postGoodService.getPostGoodState(it.uuid, userId)
            }

            PostDto(
                uuid = it.uuid,
                userId = it.userId,
                body = it.body,
                createDate = it.createDate,
                good = it.good,
                goodState = goodState,
                image = imageService.getImageByPostId(it.uuid).map {
                    it.imageKey
                },
                userImage = userService.getUserImage(it.userId).imageURL
            )
        }
    }

    // 게시글 비활성화
    @Transactional
    fun deletePost(postId: String): StatusResponseDto {

        if (!checkDeletePost(postId)) throw DisabledPostException

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


    fun checkExistPostId(postId: String): PostEntity {
        val post = postRepository.findById(postId).orElseThrow {
            throw NotExistUserIdException
        }

        return post
    }

    fun checkActivationPost(postId: String): PostEntity {
        val post = postRepository.findById(postId).orElseThrow {
            throw NotExistPostIdException
        }

        if (!post.state) throw InactivePostException

        return post
    }

}