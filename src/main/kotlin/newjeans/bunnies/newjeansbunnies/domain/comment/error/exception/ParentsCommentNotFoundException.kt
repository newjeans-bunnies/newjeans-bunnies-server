package newjeans.bunnies.newjeansbunnies.domain.comment.error.exception


import newjeans.bunnies.newjeansbunnies.domain.comment.error.CommentErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object ParentsCommentNotFoundException : CustomException(
    CommentErrorCode.NOT_EXIST_PARENTS_COMMENT_ID
)