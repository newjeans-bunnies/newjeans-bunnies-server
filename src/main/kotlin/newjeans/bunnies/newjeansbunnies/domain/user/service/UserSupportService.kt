package newjeans.bunnies.newjeansbunnies.domain.user.service

import newjeans.bunnies.newjeansbunnies.domain.user.controller.dto.response.UserSupportResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Service
@Configuration
class UserSupportService(
    @Value("\${support.country}")
    private val country: String,
    @Value("\${support.fileFormat}")
    private val fileFormat: String
) {
    private val countries = country.split(",").toList()
    private val fileFormats = fileFormat.split(",").toList()

    suspend fun execute(): UserSupportResponseDto {
        return UserSupportResponseDto(
            countries,
            fileFormats
        )
    }
}