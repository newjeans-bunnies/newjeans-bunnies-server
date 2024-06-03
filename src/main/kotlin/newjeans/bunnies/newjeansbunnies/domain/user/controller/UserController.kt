package newjeans.bunnies.newjeansbunnies.domain.user.controller

import jakarta.validation.Valid
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.request.UserUpdateRequestDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataBasicInfoResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataDetailsResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserSupportResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserUpdateResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.BlankPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.BlankUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserService
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserUpdateService
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import newjeans.bunnies.newjeansbunnies.global.security.principle.CustomUserDetails
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userUpdateService: UserUpdateService, private val userService: UserService
) {
    @GetMapping("/me")
    fun getMyData(
        @AuthenticationPrincipal auth: CustomUserDetails?,
    ): UserDataDetailsResponseDto {
        return userService.getMyData(auth?.username)
    }

    @GetMapping("/{userId}")
    fun getUserData(
        @PathVariable userId: String
    ): UserDataBasicInfoResponseDto {
        return userService.getUserData(userId)
    }

    @PatchMapping("/fix")
    suspend fun updateUser(
        @RequestParam("user-id") userId: String,
        @ModelAttribute @Valid userUpdateRequestDto: UserUpdateRequestDto,
        @RequestPart("uploadFile") multipartFiles: MultipartFile?,
    ): UserUpdateResponseDto {
        // userId Param이 비어있는지 확인
        if (userId.isBlank()) throw BlankUserIdException

        return userUpdateService.execute(userId, userUpdateRequestDto, multipartFiles)
    }

    @GetMapping("/check/userid")
    fun checkUserId(
        @RequestParam("user-id") userId: String
    ): StatusResponseDto {
        // userId Param이 비어있는지 확인
        if (userId.isBlank()) throw BlankUserIdException

        return userService.userId(userId)
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