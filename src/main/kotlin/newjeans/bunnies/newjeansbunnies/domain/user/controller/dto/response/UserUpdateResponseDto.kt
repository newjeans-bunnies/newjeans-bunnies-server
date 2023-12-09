package newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response


data class UserUpdateResponseDto(
    val id: String,
    val phoneNumber: String,
    val imageUrl: String?,
    val country: String,
    val preSignedURL: String?
)
