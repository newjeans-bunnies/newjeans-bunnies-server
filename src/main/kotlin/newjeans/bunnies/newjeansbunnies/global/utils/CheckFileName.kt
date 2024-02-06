package newjeans.bunnies.newjeansbunnies.global.utils

import newjeans.bunnies.newjeansbunnies.global.error.exception.BlankFileNameException
import newjeans.bunnies.newjeansbunnies.global.error.exception.ExtensionNotSupportedException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class CheckFileName(
    @Value("\${support.fileFormat}")
    private val fileFormat: String,
) {
    private val fileFormats = fileFormat.split(",").toSet()

    fun execute(originalFilename: String?){
        if(originalFilename.isNullOrBlank())
            throw BlankFileNameException

        val fileExtension = originalFilename.substringAfterLast(".")

        if(!fileFormats.contains(fileExtension)){
            throw ExtensionNotSupportedException
        }
    }

}