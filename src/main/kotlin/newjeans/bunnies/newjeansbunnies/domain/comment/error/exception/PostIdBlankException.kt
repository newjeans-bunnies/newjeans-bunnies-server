package newjeans.bunnies.newjeansbunnies.domain.comment.error.exception


import newjeans.bunnies.newjeansbunnies.domain.comment.error.CommentErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object PostIdBlankException : CustomException(
    CommentErrorCode.POST_ID_BLANK
)