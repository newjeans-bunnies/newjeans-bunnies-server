package newjeans.bunnies.newjeansbunnies.domain.auth.controller.dto.response


data class SignupResponseDto(
    val preSignedURL: String?,
    val status: Int,
    val message: String
)
