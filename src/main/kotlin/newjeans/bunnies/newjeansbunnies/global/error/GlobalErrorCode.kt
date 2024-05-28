package newjeans.bunnies.newjeansbunnies.global.error


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class GlobalErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {

    INVALID_TOKEN(401,"잘못된 토큰"), //잘못된 토큰
    EXPIRED_TOKEN(401, "만료된 토큰"), //만료
    UNEXPECTED_TOKEN(401, "Unexpected Token"), //무단
    INVALID_ROLE(403, "권한 없음"), //권한 없음
    REFRESH_TOKEN_NOT_FOUND_EXCEPTION(400, "RefreshToken이 존재하지 않음"),
    INTERNAL_SERVER_ERROR(500, "서버 애러"), //서버 애러
    BLANK_FILE_NAME(400, "파일이름을 확인해주세요."),
    EXTENSION_NOT_SUPPORTED(400, "지원하지 않는 확장자입니다.")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}