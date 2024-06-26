package newjeans.bunnies.newjeansbunnies.global.utils

import newjeans.bunnies.newjeansbunnies.global.error.exception.ExtensionNotSupportedException

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class CheckFileExtension(
    @Value("\${support.fileFormat}")
    private val fileFormat: String,
) {
    private val fileFormats = fileFormat.split(",").toSet()

    fun execute(extension: String){
        if(!fileFormats.contains(extension)){
            throw ExtensionNotSupportedException
        }
    }

}