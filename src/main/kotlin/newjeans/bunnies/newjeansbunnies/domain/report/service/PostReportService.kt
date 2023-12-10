package newjeans.bunnies.newjeansbunnies.domain.report.service


import newjeans.bunnies.newjeansbunnies.domain.report.controller.dto.request.ReportPostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.report.repository.PostReportRepository
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service


@Service
@Configuration
class PostReportService(
    private val postReportRepository: PostReportRepository
) {
    fun execute(reportPostRequestDto: ReportPostRequestDto){

    }
}