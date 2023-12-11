package newjeans.bunnies.newjeansbunnies.domain.report.service


import newjeans.bunnies.newjeansbunnies.domain.auth.error.exception.NotExitstIdAndPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.post.error.exception.NotExistPostIdException
import newjeans.bunnies.newjeansbunnies.domain.post.repository.PostRepository
import newjeans.bunnies.newjeansbunnies.domain.report.PostReportEntity
import newjeans.bunnies.newjeansbunnies.domain.report.controller.dto.request.ReportPostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.report.repository.PostReportRepository
import newjeans.bunnies.newjeansbunnies.domain.user.repository.UserRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.util.*


@Service
@Configuration
class PostReportService(
    private val postReportRepository: PostReportRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    fun execute(reportPostRequestDto: ReportPostRequestDto) {
        userRepository.findByUserIdAndPhoneNumber(reportPostRequestDto.id, reportPostRequestDto.phoneNumber).orElseThrow {
            throw NotExitstIdAndPhoneNumberException
        }

        postRepository.findById(reportPostRequestDto.postId).orElseThrow {
            throw NotExistPostIdException
        }

        postReportRepository.save(
            PostReportEntity(
                uuid = UUID.randomUUID().toString(),
                userId = reportPostRequestDto.id,
                phoneNumber = reportPostRequestDto.phoneNumber,
                postId = reportPostRequestDto.postId
            )
        )
    }
}