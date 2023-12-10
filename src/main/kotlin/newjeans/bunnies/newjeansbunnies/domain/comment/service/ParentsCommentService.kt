package newjeans.bunnies.newjeansbunnies.domain.comment.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.comment.ParentsCommentEntity
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.ParentsCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.ParentsCommentBasicInfoGetResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.ParentsCommentDetailGetResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.ParentsCommentSendResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.error.exception.PostNotFoundException
import newjeans.bunnies.newjeansbunnies.domain.comment.repository.ParentsCommentGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.comment.repository.ParentsCommentRepository
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.error.exception.InternalServerErrorException
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


@Service
@Configuration
class ParentsCommentService(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val parentsCommentGoodRepository: ParentsCommentGoodRepository,
    private val parentsCommentRepository: ParentsCommentRepository,
) {


    fun send(parentsCommentRequestDto: ParentsCommentRequestDto): ParentsCommentSendResponseDto {

        userRepository.findByUserId(parentsCommentRequestDto.id).orElseThrow {
            throw NotExistUserIdException
        }

        postRepository.findById(parentsCommentRequestDto.postId).orElseThrow {
            throw PostNotFoundException
        }

        val parentsCommentEntity = ParentsCommentEntity(
            uuid = UUID.randomUUID().toString(),
            id = parentsCommentRequestDto.id,
            body = parentsCommentRequestDto.body,
            good = 0L,
            postId = parentsCommentRequestDto.postId,
            createDate = LocalDateTime.now().toString()
        )

        parentsCommentRepository.save(parentsCommentEntity)

        return ParentsCommentSendResponseDto(
            uuid = parentsCommentEntity.uuid,
            id = parentsCommentEntity.id,
            body = parentsCommentEntity.body,
            postId = parentsCommentEntity.postId,
            createDate = parentsCommentEntity.createDate
        )
    }

    fun getBasicInfo(postId: String): List<ParentsCommentBasicInfoGetResponseDto> {
        postRepository.findById(postId).orElseThrow {
            throw PostNotFoundException
        }

        val data = parentsCommentRepository.findByPostId(postId).orElseThrow {
            throw InternalServerErrorException
        }

        return data.map {
            ParentsCommentBasicInfoGetResponseDto(
                uuid = it.uuid,
                id = it.id,
                body = it.body,
                createDate = it.createDate,
                postId = it.postId,
                good = it.good
            )
        }
    }

    fun getDetail(postId: String, userId: String): List<ParentsCommentDetailGetResponseDto> {

        postRepository.findById(postId).orElseThrow {
            throw NotExistPostIdException
        }

        userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }

        val data = parentsCommentRepository.findByPostId(postId).orElseThrow {
            throw NotExistPostIdException
        }


        return data.map {
            ParentsCommentDetailGetResponseDto(
                uuid = it.uuid,
                id = it.id,
                body = it.body,
                postId = it.postId,
                createDate = it.createDate,
                good = it.good,
                goodStatus = parentsCommentGoodRepository.existsByParentsCommentIdAndUserId(it.uuid, userId)
            )

        }

    }


    fun delete(id: String): StatusResponseDto {
        parentsCommentRepository.deleteById(id)
        return StatusResponseDto(
            status = 200, message = "Ok"
        )
    }
}