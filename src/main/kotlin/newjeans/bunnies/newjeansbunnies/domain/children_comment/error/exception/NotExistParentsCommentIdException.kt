package newjeans.bunnies.newjeansbunnies.domain.children_comment.error.exception


import newjeans.bunnies.newjeansbunnies.domain.children_comment.error.ChildrenCommentErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object NotExistParentsCommentIdException: CustomException(
    ChildrenCommentErrorCode.NOT_EXIST_PARENTS_COMMENT_ID
)