package newjeans.bunnies.newjeansbunnies.domain.user.error


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class UserErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {
    INACTIVE_USER(404, "비활성화된 계정입니다"),
    BLANK_NICKNAME(400, "사용자 아이디를 적어주세요."),
    BLANK_PHONENUMBER(400,"전화번호를 적어주세요."),
    RULE_VIOLATION_NICKNAME(400, "사용자 아이디는 대소문자 숫자 최대 12글자 입니다.")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}