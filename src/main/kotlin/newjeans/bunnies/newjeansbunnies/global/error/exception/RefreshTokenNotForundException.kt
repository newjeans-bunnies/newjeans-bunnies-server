package newjeans.bunnies.newjeansbunnies.global.error.exception


import newjeans.bunnies.newjeansbunnies.global.error.GlobalErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object RefreshTokenNotForundException: CustomException(
    GlobalErrorCode.REFRESH_TOKEN_NOT_FOUND_EXCEPTION
)