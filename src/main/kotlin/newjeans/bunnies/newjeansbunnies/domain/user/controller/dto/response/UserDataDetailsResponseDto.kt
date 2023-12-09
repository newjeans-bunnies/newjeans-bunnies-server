package newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response


data class UserDataDetailsResponseDto(
    val id: String,
    val phoneNumber: String,
    val country: String,
    val imageUrl: String?
)
