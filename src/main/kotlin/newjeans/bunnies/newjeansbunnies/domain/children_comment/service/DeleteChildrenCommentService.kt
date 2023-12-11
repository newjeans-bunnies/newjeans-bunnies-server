package newjeans.bunnies.newjeansbunnies.domain.children_comment.service


import jakarta.transaction.Transactional

import newjeans.bunnies.newjeansbunnies.domain.children_comment.error.exception.NotExistChildrenCommentIdException
import newjeans.bunnies.newjeansbunnies.domain.children_comment.error.exception.NotExistParentsCommentIdException
import newjeans.bunnies.newjeansbunnies.domain.children_comment.repository.ChildrenCommentGoodRepository
import newjeans.bunnies.newjeansbunnies.domain.children_comment.repository.ChildrenCommentRepository
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.repository.ParentsCommentRepository
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Service
@Configuration
class DeleteChildrenCommentService(
    private val childrenCommentRepository: ChildrenCommentRepository,
    private val parentsCommentRepository: ParentsCommentRepository,
    private val childrenCommentGoodRepository: ChildrenCommentGoodRepository
) {
    @Transactional
    fun deleteChildrenCommentByChildrenCommentId(childrenCommentId: String): StatusResponseDto {

        if (!childrenCommentRepository.existsByUuid(childrenCommentId))
            throw NotExistChildrenCommentIdException

        //대댓글 삭제
        childrenCommentRepository.deleteById(childrenCommentId)

        //대댓글 좋아요 다 삭제
        childrenCommentGoodRepository.deleteByChildrenCommentId(childrenCommentId)

        return StatusResponseDto(
            status = 204,
            message = "대댓글이 삭제됨"
        )

    }

    @Transactional
    fun deleteChildrenCommentByParentsCommentId(parentsCommentId: String): StatusResponseDto {

        val childrenCommentDataList = childrenCommentRepository.findByParentCommentId(parentsCommentId).orElseThrow {
            throw NotExistParentsCommentIdException
        }

        if (!parentsCommentRepository.existsByUuid(parentsCommentId))
            throw NotExistParentsCommentIdException

        //댓글 안에 있는 대댓글 불러오기
        for (childrenCommentData in childrenCommentDataList) {
            //대댓글 좋아요 다 삭제
            deleteChildrenCommentByChildrenCommentId(childrenCommentData.uuid)
        }

        return StatusResponseDto(
            status = 204,
            message = "대댓글이 삭제됨"
        )

    }
}