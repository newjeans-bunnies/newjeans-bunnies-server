package newjeans.bunnies.newjeansbunnies.domain.image.error


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomErrorProperty


enum class ImageErrorCode(
    private val status: Int,
    private val message: String
) : CustomErrorProperty {

    CAPACITY_EXCEEDED_IMAGE(400, "최대 10장만 가능합니다"),
    DISABLED_IMAGE(204, "비활성화된 사진입니다"),
    ACTIVATED_IMAGE(204, "활성화된 사진입니다"),
    NOT_EXIST_IMAGE(404, "존재하지 않는 사진입니다."),
    NOT_EXIST_ZONE_ID(404, "존재하지 않는 타임존");

    override fun status(): Int = status
    override fun message(): String = message
}