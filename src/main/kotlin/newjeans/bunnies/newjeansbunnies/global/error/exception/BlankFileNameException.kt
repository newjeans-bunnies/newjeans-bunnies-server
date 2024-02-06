package newjeans.bunnies.newjeansbunnies.global.error.exception


import newjeans.bunnies.newjeansbunnies.global.error.GlobalErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object BlankFileNameException: CustomException(
    GlobalErrorCode.BLANK_FILE_NAME
)