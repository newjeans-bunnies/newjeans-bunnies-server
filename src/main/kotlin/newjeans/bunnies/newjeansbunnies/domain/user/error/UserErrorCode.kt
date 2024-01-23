package newjeans.bunnies.newjeansbunnies.domain.user.error


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class UserErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {
    BLANK_USER_ID(400, "사용자 아이디를 적어주세요"),
    BLANK_PHONENUMBER(400,"전화번호를 적어주세요")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}