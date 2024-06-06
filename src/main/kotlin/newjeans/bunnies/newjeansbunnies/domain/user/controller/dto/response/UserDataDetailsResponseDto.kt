package newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response


data class UserDataDetailsResponseDto(
    val nickname: String,
    val phoneNumber: String,
    val country: String,
    val language: String,
    val imageUrl: String?,
    val birth: String
)
