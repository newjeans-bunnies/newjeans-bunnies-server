package newjeans.bunnies.newjeansbunnies.domain.auth.error.exception


import newjeans.bunnies.newjeansbunnies.domain.auth.error.AuthErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object NotExitstPhoneNumberException: CustomException(
    AuthErrorCode.NOT_EXIST_PHONE_NUMBER
)