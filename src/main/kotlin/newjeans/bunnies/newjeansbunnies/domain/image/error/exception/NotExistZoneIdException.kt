package newjeans.bunnies.newjeansbunnies.domain.image.error.exception


import newjeans.bunnies.newjeansbunnies.domain.image.error.ImageErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object NotExistZoneIdException: CustomException(
    ImageErrorCode.NOT_EXIST_ZONE_ID
)