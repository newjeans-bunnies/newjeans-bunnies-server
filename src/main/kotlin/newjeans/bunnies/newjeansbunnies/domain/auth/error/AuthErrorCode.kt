package newjeans.bunnies.newjeansbunnies.domain.auth.error


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class AuthErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {

    EXIST_USER_ID_EXCEPTION(409, "아이디가 이미 존재합니다."),
    INVALID_PASSWORD_EXCEPTION(401,"유효하지 않는 비밀번호입니다."),
    NOT_EXIST_ID(409, "존재하지 않는 아이디입니다."),
    EXIST_PHONE_NUMBER_EXCEPTION(409, "이미 등록된 전화번호입니다."),
    COUNTRY_NOT_FOUND_EXCEPTION(400, "지원하지 않거나 존재하지 않는 나라입니다."),//language
    LANGUAGE_NOT_FOUND_EXCEPTION(400, "지원하지 않거나 존재하지 않는 나라입니다."),
    FORMAT_NOT_SUPPORTED(400, "지원하지 않거나 존재하지 않는 파일 확장자입니다"),
    NOT_EXIST_PHONE_NUMBER(409, "존재하지 않는 전화번호입니다."),
    NOT_EXIST_ID_AND_PHONE_NUMBER(409, "존재하지 않는 아이디와 전화번호입니다")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}