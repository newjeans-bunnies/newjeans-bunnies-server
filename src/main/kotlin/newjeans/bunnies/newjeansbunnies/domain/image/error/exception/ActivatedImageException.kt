package newjeans.bunnies.newjeansbunnies.domain.image.error.exception


import newjeans.bunnies.newjeansbunnies.domain.image.error.ImageErrorCode
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException


object ActivatedImageException: CustomException(
    ImageErrorCode.ACTIVATED_IMAGE
)