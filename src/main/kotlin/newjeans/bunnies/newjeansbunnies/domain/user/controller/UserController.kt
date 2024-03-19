package newjeans.bunnies.newjeansbunnies.domain.user.controller


import jakarta.validation.Valid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.request.UserUpdateRequestDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.*
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.BlankPhoneNumberException
import newjeans.bunnies.newjeansbunnies.domain.user.error.exception.BlankUserIdException
import newjeans.bunnies.newjeansbunnies.domain.user.service.*
import newjeans.bunnies.newjeansbunnies.global.response.StatusResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
    private val log: Logger = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("/get-detail")
    fun getUserDetails(
        @RequestHeader("Authorization") token: String
    ): UserDataDetailsResponseDto {
        log.info(token)
        return runBlocking(Dispatchers.IO) {
            userDataDetailsService.execute(token)
        }

    }

    @GetMapping("/get-basic/{userId}")
    fun getUserBasicInfo(
        @PathVariable userId: String,
    ): UserDataBasicInfoResponseDto {
        return runBlocking(Dispatchers.IO) {
            userDataBasicInfoService.execute(userId)
        }
    }

    @GetMapping("/image/{userId}")
    fun getUserImage(
        @PathVariable userId: String
    ): UserImageResponseDto {
        return runBlocking(Dispatchers.IO) {
            userDataService.getUserImage(userId)
        }
    }

    @PatchMapping("/update")
    fun updateUser(
        @RequestParam("userid") userId: String,
        @ModelAttribute @Valid userUpdateRequestDto: UserUpdateRequestDto,
        @RequestPart("uploadFile") multipartFiles: MultipartFile?,
    ): UserUpdateResponseDto {
        if (userId.isBlank())
            throw BlankUserIdException
        return runBlocking(Dispatchers.IO) {
            userUpdateService.execute(userId, userUpdateRequestDto, multipartFiles)
        }
    }

    @GetMapping("/check/userid")
    fun checkUserId(
        @RequestParam userId: String
    ): StatusResponseDto {
        if (userId.isBlank())
            throw BlankUserIdException
        return runBlocking(Dispatchers.IO) {
            userCheckService.userId(userId)
        }
    }

    @GetMapping("/check/phonenumber")
    fun checkPhoneNumber(
        @RequestParam phonenumber: String
    ): StatusResponseDto {
        if (phonenumber.isBlank())
            throw BlankPhoneNumberException
        return runBlocking(Dispatchers.IO) {
            userCheckService.phoneNumber(phonenumber)
        }
    }


    @GetMapping("/support")
    fun checkSupport(): UserSupportResponseDto {
        return runBlocking(Dispatchers.IO) {
            userSupportService.execute()
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun deleteUser(
        @RequestParam("userid") userId: String
    ): StatusResponseDto {
        if (userId.isBlank())
            throw BlankUserIdException
        return runBlocking(Dispatchers.IO) {
            deleteUserService.execute(userId)
        }
    }
}