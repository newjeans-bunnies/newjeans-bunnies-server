package newjeans.bunnies.newjeansbunnies.domain.user.controller


import jakarta.validation.Valid
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.request.UserUpdateRequestDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.*
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.BlankPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.BlankUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.service.*
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/user")
class UserController(
    private val userDataDetailsService: UserDataDetailsService,
    private val userDataBasicInfoService: UserDataBasicInfoService,
    private val userUpdateService: UserUpdateService,
    private val deleteUserService: DeleteUserService,
    private val userCheckService: UserCheckService,
    private val userSupportService: UserSupportService,
    private val userDataService: UserDataService
) {
    @GetMapping("/get-detail")
    suspend fun getUserDetails(
        @RequestHeader("Authorization") token: String
    ): UserDataDetailsResponseDto {
        return userDataDetailsService.execute(token)
    }

    @GetMapping("/get-basic/{userId}")
    suspend fun getUserBasicInfo(
        @PathVariable userId: String,
    ): UserDataBasicInfoResponseDto {
        return userDataBasicInfoService.execute(userId)
    }

    @GetMapping("/image/{userId}")
    suspend fun getUserImage(
        @PathVariable userId: String
    ): UserImageResponseDto {
        return userDataService.getUserImage(userId)
    }

    @PatchMapping("/update")
    suspend fun updateUser(
        @RequestParam("userid") userId: String,
        @ModelAttribute @Valid userUpdateRequestDto: UserUpdateRequestDto,
        @RequestPart("uploadFile") multipartFiles: MultipartFile?,
    ): UserUpdateResponseDto {
        if (userId.isBlank())
            throw BlankUserIdException
        return userUpdateService.execute(userId, userUpdateRequestDto, multipartFiles)
    }

    @GetMapping("/check/userid")
    suspend fun checkUserId(
        @RequestParam userId: String
    ): StatusResponseDto {
        if (userId.isBlank())
            throw BlankUserIdException
        return userCheckService.userId(userId)
    }

    @GetMapping("/check/phonenumber")
    suspend fun checkPhoneNumber(
        @RequestParam phonenumber: String
    ): StatusResponseDto {
        if (phonenumber.isBlank())
            throw BlankPhoneNumberException
        return userCheckService.phoneNumber(phonenumber)
    }


    @GetMapping("/support")
    suspend fun checkSupport(): UserSupportResponseDto {
        return userSupportService.execute()
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    suspend fun deleteUser(
        @RequestParam userId: String
    ): StatusResponseDto {
        if (userId.isBlank())
            throw BlankUserIdException
        return deleteUserService.execute(userId)
    }
}