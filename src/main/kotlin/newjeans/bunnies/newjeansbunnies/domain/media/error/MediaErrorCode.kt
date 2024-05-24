package newjeans.bunnies.newjeansbunnies.domain.media.error


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class MediaErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {
;

    override fun status(): Int = status
    override fun message(): String = message
}