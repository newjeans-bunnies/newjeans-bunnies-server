package newjeans.bunnies.newjeansbunnies.domain.post.error


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class PostErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {
    NOT_EXIST_USER(400, "Not Exist User"),
    POST_NOT_FOUND(400, "Not Found Post"),
    ;

    override fun status(): Int = status
    override fun message(): String = message
}