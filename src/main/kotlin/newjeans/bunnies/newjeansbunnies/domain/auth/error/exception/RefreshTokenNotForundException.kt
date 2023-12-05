package newjeans.bunnies.newjeansbunnies.domain.auth.error.exception

import newjeans.bunnies.newjeansbunnies.domain.auth.error.AuthErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException

object RefreshTokenNotForundException: CustomException(
    AuthErrorCode.REFRESH_TOKEN_NOT_FOUND_EXCEPTION
)