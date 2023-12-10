package newjeans.bunnies.newjeansbunnies.domain.image.error


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class ImageErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {
    IMAGE_EXTENSION_NOT_FOUND(400, "Image Extension Not Found"), //사진 확장자가 없음
    IMAGE_DELETE_EXCEPTION(500, "유저 사진이 지워지지 않음")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}