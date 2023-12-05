package newjeans.bunnies.newjeansbunnies.global.security.error.exception

import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException
import newjeans.bunnies.newjeansbunnies.global.security.error.GlobalErrorCode

object InvalidTokenException: CustomException(
    GlobalErrorCode.INVALID_TOKEN
)