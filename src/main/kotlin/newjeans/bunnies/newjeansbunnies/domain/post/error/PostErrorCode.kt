package newjeans.bunnies.newjeansbunnies.domain.post.error


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class PostErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {
    NOT_EXIST_POST_ID(409, "존재하지 않는 게시글아이디 입니다."),
    OVER_FILE(400, "사진 최대 갯수는 10개 입니다."),
    NOT_EXIST_POST_IMAGE(200, "게시글에 사진이 존재하지 않습니다")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}