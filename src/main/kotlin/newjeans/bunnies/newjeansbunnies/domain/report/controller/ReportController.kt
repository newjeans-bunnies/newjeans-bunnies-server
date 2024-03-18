package newjeans.bunnies.newjeansbunnies.domain.report.controller


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import newjeans.bunnies.newjeansbunnies.domain.report.controller.dto.request.ReportPostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.report.service.PostReportService

import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@Configuration
@RequestMapping("/api/report")
class ReportController(
    private val postReportService: PostReportService,
) {
    @PostMapping("/post")
    fun reportPost(
        @RequestBody reportPostRequestDto: ReportPostRequestDto
    ) {
        runBlocking(Dispatchers.IO) { postReportService.execute(reportPostRequestDto)}
    }
}