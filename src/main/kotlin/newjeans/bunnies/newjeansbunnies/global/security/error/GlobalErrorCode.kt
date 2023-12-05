package newjeans.bunnies.newjeansbunnies.global.security.error

import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty

enum class GlobalErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {

    INVALID_TOKEN(401,"Invalid Token"), //잘못된 토큰
    EXPIRED_TOKEN(401, "Expired Token"), //만료
    UNEXPECTED_TOKEN(401, "Unexpected Token"), //무단
    INVALID_ROLE(401, "Invalid Role"), //권한 없음

    INTERNAL_SERVER_ERROR(500, "Internal Server Error") //서버 애러
    ;

    override fun status(): Int = status
    override fun message(): String = message
}