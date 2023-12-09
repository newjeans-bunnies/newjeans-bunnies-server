package newjeans.bunnies.newjeansbunnies.domain.post.error


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class PostErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {
    POST_NOT_FOUND(409, "게시글이 없습니다.")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}