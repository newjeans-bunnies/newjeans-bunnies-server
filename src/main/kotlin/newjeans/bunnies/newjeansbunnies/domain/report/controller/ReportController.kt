package newjeans.bunnies.newjeansbunnies.domain.report.controller


import newjeans.bunnies.newjeansbunnies.domain.report.controller.dto.request.ReportPostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.report.service.ReportService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.global.security.principle.CustomUserDetails

import org.springframework.context.annotation.Configuration
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@Configuration
@RequestMapping("/api/report")
class ReportController(
    private val reportService: ReportService,
) {
    @PostMapping("/post")
    fun reportPost(
        @RequestBody reportPostRequestDto: ReportPostRequestDto,
        @AuthenticationPrincipal auth: CustomUserDetails?
    ): StatusResponseDto {
        return reportService.postReport(reportPostRequestDto, auth?.username)
    }
}