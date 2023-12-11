package newjeans.bunnies.newjeansbunnies.domain.report.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExitstIdAndPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.error.exception.NotExistParentsCommentIdException
import newjeans.bunnies.newjeansbunnies.domain.parents_comment.repository.ParentsCommentRepository
import newjeans.bunnies.newjeansbunnies.domain.report.ParentsCommentReportEntity
import newjeans.bunnies.newjeansbunnies.domain.report.controller.dto.request.ReportParentsCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.report.repository.ParentsCommentReportRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.util.*


@Service
@Configuration
class ParentsCommentReportService(
    private val parentsCommentReportRepository: ParentsCommentReportRepository,
    private val parentsCommentRepository: ParentsCommentRepository,
    private val userRepository: UserRepository
) {
    fun execute(reportParentsCommentRequestDto: ReportParentsCommentRequestDto) {

        userRepository.findByUserIdAndPhoneNumber(reportParentsCommentRequestDto.userId, reportParentsCommentRequestDto.phoneNumber).orElseThrow {
            throw NotExitstIdAndPhoneNumberException
        }

        parentsCommentRepository.findById(reportParentsCommentRequestDto.parentsCommentId).orElseThrow {
            throw NotExistParentsCommentIdException
        }

        parentsCommentReportRepository.save(
            ParentsCommentReportEntity(
                uuid = UUID.randomUUID().toString(),
                userId = reportParentsCommentRequestDto.userId,
                phoneNumber = reportParentsCommentRequestDto.phoneNumber,
                parentsCommentId = reportParentsCommentRequestDto.parentsCommentId
            )
        )

    }
}