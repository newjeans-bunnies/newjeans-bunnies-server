package newjeans.bunnies.newjeansbunnies.domain.parents_comment.service


import jakarta.transaction.Transactional

import newjeans.bunnies.newjeansbunnies.domain.children_comment.service.DeleteChildrenCommentService
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.error.exception.NotExistParentsCommentIdException
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.repository.ParentsCommentGoodRepository

import newjeans.bunnies.newjeansbunnies.domain.parents_comment.repository.ParentsCommentRepository
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Service
@Configuration
class DeleteParentsCommentService(
    private val parentsCommentRepository: ParentsCommentRepository,
    private val parentsCommentGoodRepository: ParentsCommentGoodRepository,
    private val deleteChildrenCommentService: DeleteChildrenCommentService,
    private val postRepository: PostRepository
) {
    @Transactional
    fun deleteParentsCommentByParentsCommentId(parentsCommentId: String): StatusResponseDto {

        println(parentsCommentId)
        parentsCommentRepository.findById(parentsCommentId).orElseThrow {
            throw NotExistParentsCommentIdException
        }

        //댓글 좋아요 삭제
        parentsCommentGoodRepository.deleteByParentsCommentId(parentsCommentId)

        deleteChildrenCommentService.deleteChildrenCommentByParentsCommentId(parentsCommentId)

        //댓글 삭제
        parentsCommentRepository.deleteById(parentsCommentId)

        return StatusResponseDto(
            status = 204,
            message = "댓글이 삭제됨"
        )
    }

    @Transactional
    fun deleteParentsCommentByPostId(postId: String): StatusResponseDto {

        postRepository.findById(postId).orElseThrow {
            throw NotExistPostIdException
        }

        //게시판에 적혀 있는 댓글 정보가져오기
        val parentsCommentDataList = parentsCommentRepository.findByPostId(postId).orElseThrow {
            throw NotExistPostIdException
        }

        //댓글 정보
        for (parentsCommentData in parentsCommentDataList) {
            deleteParentsCommentByParentsCommentId(parentsCommentData.uuid)
        }

        return StatusResponseDto(
            status = 204,
            message = "댓글이 삭제됨"
        )
    }
}