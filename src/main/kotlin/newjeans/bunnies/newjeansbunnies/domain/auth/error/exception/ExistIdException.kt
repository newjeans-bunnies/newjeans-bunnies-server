package newjeans.bunnies.newjeansbunnies.domain.auth.error.exception


import newjeans.bunnies.newjeansbunnies.domain.auth.error.AuthErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object ExistIdException: CustomException(
    AuthErrorCode.EXIST_ID_EXCEPTION
)