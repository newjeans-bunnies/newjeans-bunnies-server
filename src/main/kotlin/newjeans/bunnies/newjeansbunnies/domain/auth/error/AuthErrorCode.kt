package newjeans.bunnies.newjeansbunnies.domain.auth.error

import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class AuthErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {

    EXIST_ID_EXCEPTION(400, "Exist ID"),
    INVALID_PASSWORD_EXCEPTION(400,"Invalid Password"),
    NOT_EXIST_USER(400, "Not Exist User"),
    REFRESH_TOKEN_NOT_FOUND_EXCEPTION(400, "Not Found RefreshToken")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}