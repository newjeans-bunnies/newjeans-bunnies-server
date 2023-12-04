package newjeans.bunnies.newjeansbunnies.domain.post.error

import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class PostErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {

    ;

    override fun status(): Int = status
    override fun message(): String = message
}