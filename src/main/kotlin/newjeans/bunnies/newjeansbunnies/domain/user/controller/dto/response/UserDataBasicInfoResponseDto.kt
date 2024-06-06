package newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response


data class UserDataBasicInfoResponseDto(
    val nickname: String,
    val country: String,
    val imageUrl: String?
)