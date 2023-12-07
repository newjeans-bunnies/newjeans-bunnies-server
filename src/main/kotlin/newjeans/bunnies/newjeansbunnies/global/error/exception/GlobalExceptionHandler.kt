package newjeans.bunnies.newjeansbunnies.global.error.exception


import newjeans.bunnies.newjeansbunnies.global.error.custom.CustomException
import newjeans.bunnies.newjeansbunnies.global.error.response.ErrorResponse

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleEmptyResultDataAccessException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse>{
        val errorMessage: String? = ex.bindingResult
            .allErrors[0]
            .defaultMessage
        return ResponseEntity(
            ErrorResponse(
                400,
                errorMessage
            ), HttpStatus.BAD_REQUEST
        )
    }


    @ExceptionHandler(CustomException::class)
    protected fun handleBindException(ex: CustomException): ResponseEntity<ErrorResponse> {
        val httpStatus = when (ex.errorProperty.status()) {
            400 -> HttpStatus.BAD_REQUEST
            401 -> HttpStatus.UNAUTHORIZED
            403 -> HttpStatus.FORBIDDEN
            404 -> HttpStatus.NOT_FOUND
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