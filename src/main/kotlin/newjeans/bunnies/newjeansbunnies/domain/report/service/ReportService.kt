package newjeans.bunnies.newjeansbunnies.domain.report.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExitstIdAndPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.report.PostReportEntity
import newjeans.bunnies.newjeansbunnies.domain.report.controller.dto.request.ReportPostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.report.repository.PostReportRepository
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.NotExistUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import newjeans.bunnies.newjeansbunnies.global.error.exception.InvalidRoleException
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*


@Service
@Configuration
class ReportService(
    private val postReportRepository: PostReportRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    fun postReport(reportPostRequestDto: ReportPostRequestDto, authorizedUser: String?): StatusResponseDto {

        if (reportPostRequestDto.userId != authorizedUser) throw InvalidRoleException

        if(!userRepository.existsById(reportPostRequestDto.userId)) throw NotExistUserIdException

        userRepository.findByNicknameAndPhoneNumber(reportPostRequestDto.userId, reportPostRequestDto.phoneNumber)
            .orElseThrow {
                throw NotExitstIdAndPhoneNumberException
            }

        postRepository.findByIdOrNull(reportPostRequestDto.postId) ?: throw NotExistPostIdException

        postReportRepository.save(
            PostReportEntity(
                id = UUID.randomUUID().toString(),
                nickname = reportPostRequestDto.userId,
                postId = reportPostRequestDto.postId,
                body = reportPostRequestDto.body
            )
        )

        return StatusResponseDto(200, "OK")
    }
}