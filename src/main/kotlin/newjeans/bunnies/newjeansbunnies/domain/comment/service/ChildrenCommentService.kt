package newjeans.bunnies.newjeansbunnies.domain.comment.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.comment.ChildrenCommentEntity
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.request.ChildrenCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.ChildrenCommentBasicGetResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.ChildrenCommentDetailGetResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.ChildrenCommentSendResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.controller.dto.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.domain.comment.error.exception.NotExistParentsCommentIdException
import newjeans.bunnies.newjeansbunnies.domain.comment.error.exception.ParentsCommentNotFoundException
import newjeans.bunnies.newjeansbunnies.domain.comment.repository.ChildrenCommentGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.comment.repository.ChildrenCommentRepository
import newjeans.bunnies.newjeansbunnies.domain.comment.repository.ParentsCommentRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


@Service
@Configuration
class ChildrenCommentService(
    private val childrenCommentGoodRepository: ChildrenCommentGoodRepository,
    private val childrenCommentRepository: ChildrenCommentRepository,
    private val parentsCommentRepository: ParentsCommentRepository,
    private val userRepository: UserRepository
) {

    fun send(data: ChildrenCommentRequestDto): ChildrenCommentSendResponseDto {

        parentsCommentRepository.findById(data.parentId).orElseThrow {
            throw ParentsCommentNotFoundException
        }

        userRepository.findByUserId(data.id).orElseThrow {
            throw NotExistUserIdException
        }

        val childrenCommentEntity = ChildrenCommentEntity(
            uuid = UUID.randomUUID().toString(),
            id = data.id,
            createDate = LocalDateTime.now().toString(),
            body = data.body,
            parentCommentId = data.parentId,
            good = 0L
        )
        childrenCommentRepository.save(
            childrenCommentEntity
        )
        return ChildrenCommentSendResponseDto(
            uuid = childrenCommentEntity.uuid,
            id = childrenCommentEntity.id,
            body = childrenCommentEntity.body,
            createDate = childrenCommentEntity.createDate,
            parentsCommentId = childrenCommentEntity.parentCommentId
        )
    }

    fun getBasicInfo(parentsCommentId: String): List<ChildrenCommentBasicGetResponseDto> {
        parentsCommentRepository.findById(parentsCommentId).orElseThrow {
            throw NotExistParentsCommentIdException
        }

        val data = childrenCommentRepository.findByParentCommentId(parentsCommentId).orElseThrow {
            throw NotExistParentsCommentIdException
        }

        return data.map {
            ChildrenCommentBasicGetResponseDto(
                uuid = it.uuid,
                id = it.id,
                body = it.body,
                good = it.good,
                parentsCommentId = it.parentCommentId,
                createDate = it.createDate
            )
        }

    }

    fun getDetail(parentsCommentId: String, userId: String): List<ChildrenCommentDetailGetResponseDto> {

        parentsCommentRepository.findById(parentsCommentId).orElseThrow {
            throw NotExistParentsCommentIdException
        }

        userRepository.findByUserId(userId).orElseThrow {
            throw NotExistUserIdException
        }

        val data = childrenCommentRepository.findByParentCommentId(parentsCommentId).orElseThrow {
            throw NotExistParentsCommentIdException
        }

        return data.map {
            ChildrenCommentDetailGetResponseDto(
                uuid = it.uuid,
                id = it.id,
                body = it.body,
                good = it.good,
                goodStatus = childrenCommentGoodRepository.existsByChildrenCommentIdAndUserId(it.uuid, userId),
                parentsCommentId = it.parentCommentId,
                createDate = it.createDate
            )
        }

    }


    fun delete(id: String): StatusResponseDto {
        return StatusResponseDto(
            status = 200,
            message = "Ok"
        )
    }
}