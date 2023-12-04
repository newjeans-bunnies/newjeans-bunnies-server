package newjeans.bunnies.newjeansbunnies.global.error.exception
import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException
import newjeans.bunnies.newjeansbunnies.global.error.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.multipart.MaxUploadSizeExceededException

@ControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(this.javaClass)!!

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException::class)
    protected fun maxUploadSizeExceededException(e: MaxUploadSizeExceededException): ErrorResponse {
        return ErrorResponse(
            status = 500,
            message = e.message.toString()
        )
    }


    @ExceptionHandler(CustomException::class)
    protected fun handleBindException(ex: CustomException): ResponseEntity<ErrorResponse> {
        val httpStatus = when (ex.errorProperty.status()) {
            404 -> HttpStatus.NOT_FOUND
            400 -> HttpStatus.BAD_REQUEST
            403 -> HttpStatus.FORBIDDEN
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity(
            ErrorResponse(
                status = ex.errorProperty.status(),
                message = ex.errorProperty.message()
            ), httpStatus
        )
    }
}