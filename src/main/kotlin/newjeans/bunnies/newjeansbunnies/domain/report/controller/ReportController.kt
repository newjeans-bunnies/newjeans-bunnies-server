package newjeans.bunnies.newjeansbunnies.domain.report.controller


import newjeans.bunnies.newjeansbunnies.domain.report.controller.dto.request.ReportChildrenCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.report.controller.dto.request.ReportParentsCommentRequestDto
import newjeans.bunnies.newjeansbunnies.domain.report.controller.dto.request.ReportPostRequestDto
import newjeans.bunnies.newjeansbunnies.domain.report.service.ChildrenCommentReportService
import newjeans.bunnies.newjeansbunnies.domain.report.service.ParentsCommentReportService
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
    private val parentsCommentReportService: ParentsCommentReportService,
    private val childrenCommentReportService: ChildrenCommentReportService,
    private val postReportService: PostReportService,
) {

    @PostMapping("/children-comment")
    fun reportChildrenComment(
        @RequestBody reportChildrenCommentRequestDto: ReportChildrenCommentRequestDto
    ) {
        childrenCommentReportService.execute(reportChildrenCommentRequestDto)
    }

    @PostMapping("/parents-comment")
    fun reportParentsComment(
        @RequestBody reportParentsCommentRequestDto: ReportParentsCommentRequestDto
    ) {
        parentsCommentReportService.execute(reportParentsCommentRequestDto)
    }


    @PostMapping("/post")
    fun reportPost(
        @RequestBody reportPostRequestDto: ReportPostRequestDto
    ) {
        postReportService.execute(reportPostRequestDto)
    }
}