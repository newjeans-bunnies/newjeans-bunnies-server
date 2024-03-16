package newjeans.bunnies.newjeansbunnies.domain.post.service


import newjeans.bunnies.newjeansbunnies.domain.post.PostImageEntity
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
    suspend fun execute(postId: String): List<PostImageResponseDto> {

        checkValidPostId(postId)

        val imageData = getPostImage(postId)

        return imageData.map {
            PostImageResponseDto(
                imageURL = it.imageUrl
            )
        }
    }

    private suspend fun checkValidPostId(postId: String){
        postRepository.findByUuid(postId).orElseThrow {
            throw NotExistPostIdException
        }
    }


    private suspend fun getPostImage(postId: String): List<PostImageEntity>{
        return postImageRepository.findByPostId(postId).orElseThrow {
            throw NotExistPostImageException
        }
    }

}