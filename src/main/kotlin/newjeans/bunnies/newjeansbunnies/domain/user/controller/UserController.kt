package newjeans.bunnies.newjeansbunnies.domain.user.controller

import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.request.UserUpdateRequestDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataBasicInfoResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataDetailsResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserSupportResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.BlankPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.BlankUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.global.security.principle.CustomUserDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import retrofit2.http.Body

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/me")
    fun getMyData(
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): UserDataDetailsResponseDto {
        return userService.getMyData(auth.username)
    }

    @GetMapping("/{userId}")
    fun getUserData(
        @PathVariable userId: String
    ): UserDataBasicInfoResponseDto {
        return userService.getUserData(userId)
    }

    @PatchMapping("/fix")
    suspend fun updateUser(
        @AuthenticationPrincipal auth: CustomUserDetails?,
        @Body userUpdateRequestDto: UserUpdateRequestDto,
    ): StatusResponseDto {
        return userService.updateUserImage(auth?.username, userUpdateRequestDto)
    }

    @GetMapping("/check/nickname")
    fun checkNickname(
        @RequestParam("nickname") nickname: String
    ): StatusResponseDto {

        if (nickname.isBlank()) throw BlankUserIdException

        return userService.nickname(nickname)
    }

    @GetMapping("/check/phonenumber")
    fun checkPhoneNumber(
        @RequestParam phonenumber: String
    ): StatusResponseDto {

        // phonenumber Param이 비어있는지 확인
        if (phonenumber.isBlank()) throw BlankPhoneNumberException

        return userService.phoneNumber(phonenumber)
    }

    @GetMapping("/support")
    fun checkSupport(): UserSupportResponseDto {
        return userService.getSupport()
    }

}