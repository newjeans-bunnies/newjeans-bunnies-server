package newjeans.bunnies.newjeansbunnies.domain.auth.error.exception


import newjeans.bunnies.newjeansbunnies.domain.auth.error.AuthErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object NotExitstIdAndPhoneNumberException: CustomException(
    AuthErrorCode.NOT_EXIST_ID_AND_PHONE_NUMBER
)