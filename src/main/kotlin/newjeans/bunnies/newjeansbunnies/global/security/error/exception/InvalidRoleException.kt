package newjeans.bunnies.newjeansbunnies.global.security.error.exception

import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException
import newjeans.bunnies.newjeansbunnies.global.security.error.GlobalErrorCode

object InvalidRoleException: CustomException(
    GlobalErrorCode.INVALID_ROLE
)