package newjeans.bunnies.newjeansbunnies.domain.post.service


import newjeans.bunnies.newjeansbunnies.domain.post.controller.dto.response.PostImageResponseDto
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostImageException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostImageRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Configuration
@Service
class GetPostImageService(
    private val postRepository: PostRepository,
    private val postImageRepository: PostImageRepository
) {
    fun execute(postId: String): List<PostImageResponseDto> {
        postRepository.findById(postId).orElseThrow {
            throw NotExistPostIdException
        }

        val imageData = postImageRepository.findByPostId(postId).orElseThrow {
            throw NotExistPostImageException
        }

        return imageData.map {
            PostImageResponseDto(
                imageURL = it.imageUrl
            )
        }
    }
}