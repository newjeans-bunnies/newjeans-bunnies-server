package newjeans.bunnies.newjeansbunnies.domain.auth.error.exception


import newjeans.bunnies.newjeansbunnies.domain.auth.error.AuthErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object ExistPhoneNumberException: CustomException(
    AuthErrorCode.EXIST_PHONE_NUMBER_EXCEPTION
)