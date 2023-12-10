package newjeans.bunnies.newjeansbunnies.domain.report.error


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class ReportErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {
    ;

    override fun status(): Int = status
    override fun message(): String = message
}