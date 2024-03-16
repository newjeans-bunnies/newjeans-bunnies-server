package newjeans.bunnies.newjeansbunnies.domain.report.repository

import newjeans.bunnies.newjeansbunnies.domain.report.PostReportEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PostReportRepository: CrudRepository<PostReportEntity, String>