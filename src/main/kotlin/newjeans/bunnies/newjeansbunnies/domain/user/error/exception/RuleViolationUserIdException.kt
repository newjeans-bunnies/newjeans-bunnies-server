package newjeans.bunnies.newjeansbunnies.domain.user.error.exception


import newjeans.bunnies.newjeansbunnies.domain.user.error.UserErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object RuleViolationUserIdException: CustomException(
    UserErrorCode.RULE_VIOLATION_USER_ID
)