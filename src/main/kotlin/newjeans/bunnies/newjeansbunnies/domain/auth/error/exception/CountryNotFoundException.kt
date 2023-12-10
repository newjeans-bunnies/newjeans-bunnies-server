package newjeans.bunnies.newjeansbunnies.domain.auth.error.exception


import newjeans.bunnies.newjeansbunnies.domain.auth.error.AuthErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object CountryNotFoundException: CustomException(
    AuthErrorCode.COUNTRY_NOT_FOUND_EXCEPTION
)