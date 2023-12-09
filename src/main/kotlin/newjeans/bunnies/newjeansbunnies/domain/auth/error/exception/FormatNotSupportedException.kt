package newjeans.bunnies.newjeansbunnies.domain.auth.error.exception


import newjeans.bunnies.newjeansbunnies.domain.auth.error.AuthErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object FormatNotSupportedException: CustomException(
    AuthErrorCode.FORMAT_NOT_SUPPORTED
)