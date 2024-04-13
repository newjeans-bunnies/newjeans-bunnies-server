package newjeans.bunnies.newjeansbunnies.domain.image.controller.dto.response

import java.net.URL

data class CreatePreSignedUrlResponse(
    val presignedUrl: URL
)
