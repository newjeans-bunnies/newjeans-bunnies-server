package newjeans.bunnies.newjeansbunnies.domain.parents_comment.error.exception


import newjeans.bunnies.newjeansbunnies.domain.parents_comment.error.ParentsCommentErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object NotExistParentsCommentIdException: CustomException(
    ParentsCommentErrorCode.NOT_EXIST_PARENTS_COMMENT_ID
)