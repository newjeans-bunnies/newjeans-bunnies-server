package newjeans.bunnies.newjeansbunnies.domain.post.error

import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class PostErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {
    NOT_EXIST_USER(400, "Not Exist User"),
    ;

    override fun status(): Int = status
    override fun message(): String = message
}