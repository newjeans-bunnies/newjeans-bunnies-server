package newjeans.bunnies.newjeansbunnies.domain.auth.error.exception

import newjeans.bunnies.newjeansbunnies.domain.auth.error.AuthErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException
import newjeans.bunnies.newjeansbunnies.global.security.error.GlobalErrorCode

object ExistIdException: CustomException(
    AuthErrorCode.EXIST_ID_EXCEPTION
)