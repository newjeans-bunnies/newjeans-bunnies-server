package newjeans.bunnies.newjeansbunnies.domain.user.controller


import jakarta.validation.Valid
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.request.UserUpdateRequestDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataBasicInfoResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserDataDetailsResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserUpdateResponseDto
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserDataBasicInfoService
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserDataDetailsService
import newjeans.bunnies.newjeansbunnies.domain.user.service.UserUpdateService
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.*


@RestController
@Configuration
@RequestMapping("/api/user")
class UserController(
    private val userDataDetailsService: UserDataDetailsService,
    private val userDataBasicInfoService: UserDataBasicInfoService,
    private val userUpdateService: UserUpdateService
) {
    @GetMapping("/get-detail/{id}")
    fun getUserDetails(
        @PathVariable id: String,
        @RequestHeader("Authorization") token: String
    ): UserDataDetailsResponseDto {
        return userDataDetailsService.execute(id, token)
    }
    @GetMapping("/get-basic/{id}")
    fun getUserBasicInfo(
        @PathVariable id: String,
    ): UserDataBasicInfoResponseDto {
        return userDataBasicInfoService.execute(id)
    }

    @PatchMapping("/update")
    fun updateUser(
        @RequestBody @Valid userUpdateRequestDto: UserUpdateRequestDto,
        @RequestParam id: String
    ): UserUpdateResponseDto {
        return userUpdateService.execute(id, userUpdateRequestDto)
    }
}