package newjeans.bunnies.newjeansbunnies.domain.post.error.exception


import newjeans.bunnies.newjeansbunnies.domain.post.error.PostErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object DisabledPostException: CustomException(
    PostErrorCode.DISABLED_POST
)