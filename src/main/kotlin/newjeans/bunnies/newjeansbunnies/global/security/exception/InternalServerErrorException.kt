package newjeans.bunnies.newjeansbunnies.global.security.exception


import newjeans.bunnies.newjeansbunnies.global.error.GlobalErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object InternalServerErrorException: CustomException(
    GlobalErrorCode.INTERNAL_SERVER_ERROR
)