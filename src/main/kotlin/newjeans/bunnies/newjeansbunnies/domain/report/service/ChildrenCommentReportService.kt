package newjeans.bunnies.newjeansbunnies.domain.report.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExitstIdAndPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.children_comment.error.exception.NotExistChildrenCommentIdException
import newjeans.bunnies.newjeansbunnies.domain.children_comment.repository.ChildrenCommentRepository
import newjeans.bunnies.newjeansbunnies.domain.report.ChildrenCommentReportEntity
import newjeans.bunnies.newjeansbunnies.domain.report.controller.dto.request.ReportChildrenCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.report.repository.ChildrenCommentReportRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.util.*


@Service
@Configuration
class ChildrenCommentReportService(
    private val childrenCommentReportRepository: ChildrenCommentReportRepository,
    private val childrenCommentRepository: ChildrenCommentRepository,
    private val userRepository: UserRepository
) {
    fun execute(reportChildrenCommentRequestDto: ReportChildrenCommentRequestDto) {

        userRepository.findByUserIdAndPhoneNumber(reportChildrenCommentRequestDto.userId, reportChildrenCommentRequestDto.phoneNumber).orElseThrow {
            throw NotExitstIdAndPhoneNumberException
        }

        childrenCommentRepository.findById(reportChildrenCommentRequestDto.childrenCommentId).orElseThrow {
            throw NotExistChildrenCommentIdException
        }

        childrenCommentReportRepository.save(
            ChildrenCommentReportEntity(
                uuid = UUID.randomUUID().toString(),
                userId = reportChildrenCommentRequestDto.userId,
                phoneNumber = reportChildrenCommentRequestDto.phoneNumber,
                childrenCommentId = reportChildrenCommentRequestDto.childrenCommentId
            )
        )
    }
}