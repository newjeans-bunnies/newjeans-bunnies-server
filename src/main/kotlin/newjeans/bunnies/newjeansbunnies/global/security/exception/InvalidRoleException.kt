package newjeans.bunnies.newjeansbunnies.global.security.exception


import newjeans.bunnies.newjeansbunnies.global.error.GlobalErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object InvalidRoleException: CustomException(
    GlobalErrorCode.INVALID_ROLE
)