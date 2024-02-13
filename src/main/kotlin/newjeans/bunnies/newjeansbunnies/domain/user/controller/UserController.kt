package newjeans.bunnies.newjeansbunnies.domain.user.controller


import jakarta.validation.Valid
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.request.UserUpdateRequestDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataBasicInfoResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataDetailsResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserUpdateResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.BlankPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.BlankUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.service.*
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@Configuration
@RequestMapping("/api/user")
class UserController(
    private val userDataDetailsService: UserDataDetailsService,
    private val userDataBasicInfoService: UserDataBasicInfoService,
    private val userUpdateService: UserUpdateService,
    private val deleteUserService: DeleteUserService,
    private val userCheckService: UserCheckService
) {
    @GetMapping("/get-detail")
    fun getUserDetails(
        @RequestHeader("Authorization") token: String
    ): UserDataDetailsResponseDto {
        return userDataDetailsService.execute(token)
    }

    @GetMapping("/get-basic/{userId}")
    fun getUserBasicInfo(
        @PathVariable userId: String,
    ): UserDataBasicInfoResponseDto {
        return userDataBasicInfoService.execute(userId)
    }

    @PatchMapping("/update")
    fun updateUser(
        @RequestParam("userid") userId: String,
        @ModelAttribute @Valid userUpdateRequestDto: UserUpdateRequestDto,
        @RequestPart("uploadFile") multipartFiles: MultipartFile?,
    ): UserUpdateResponseDto {
        if (userId.isBlank())
            throw BlankUserIdException
        return userUpdateService.execute(userId, userUpdateRequestDto, multipartFiles)
    }

    @GetMapping("/check/userid")
    fun checkUserId(
        @RequestParam userId: String
    ): StatusResponseDto {
        if(userId.isBlank())
            throw BlankUserIdException
        return userCheckService.userId(userId)
    }

    @GetMapping("/check/phonenumber")
    fun checkPhoneNumber(
        @RequestParam phonenumber: String
    ): StatusResponseDto {
        if(phonenumber.isBlank())
            throw BlankPhoneNumberException
        return userCheckService.phoneNumber(phonenumber)
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun deleteUser(
        @RequestParam userId: String
    ): StatusResponseDto {
        if (userId.isBlank())
            throw BlankUserIdException
        return deleteUserService.execute(userId)
    }
}