package newjeans.bunnies.newjeansbunnies.domain.children_comment.error.exception


import newjeans.bunnies.newjeansbunnies.domain.children_comment.error.ChildrenCommentErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object CommentIdBlankException : CustomException(
    ChildrenCommentErrorCode.COMMENT_ID_BLANK
)