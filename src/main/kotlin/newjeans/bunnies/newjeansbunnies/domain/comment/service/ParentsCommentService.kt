package newjeans.bunnies.newjeansbunnies.domain.comment.service


import newjeans.bunnies.newjeansbunnies.domain.comment.ParentsCommentEntity
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.ParentsCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.error.exception.PostNotFoundException
import newjeans.bunnies.newjeansbunnies.domain.comment.error.exception.UserNotFoundException
import newjeans.bunnies.newjeansbunnies.domain.comment.repository.ParentsCommentRepository
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID


@Service
@Configuration
class ParentsCommentService(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val parentsCommentRepository: ParentsCommentRepository
) {
    fun send(parentsCommentRequestDto: ParentsCommentRequestDto) {

        userRepository.findById(parentsCommentRequestDto.id).orElseThrow {
            throw UserNotFoundException
        }

        postRepository.findById(parentsCommentRequestDto.postId).orElseThrow {
            throw PostNotFoundException
        }

        parentsCommentRepository.save(
            ParentsCommentEntity(
                uuid = UUID.randomUUID().toString(),
                id = parentsCommentRequestDto.id,
                body = parentsCommentRequestDto.body,
                good = 0L,
                postId = parentsCommentRequestDto.postId,
                createDate = LocalDateTime.now().toString()
            )
        )
    }

    fun delete(id: String) {
        parentsCommentRepository.deleteById(id)
    }
}