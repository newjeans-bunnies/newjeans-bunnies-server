package newjeans.bunnies.newjeansbunnies.domain.comment.error.exception


import newjeans.bunnies.newjeansbunnies.domain.comment.error.CommentErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object NoEditPermissionException: CustomException(
    CommentErrorCode.NO_EDIT_PERMISSION
)