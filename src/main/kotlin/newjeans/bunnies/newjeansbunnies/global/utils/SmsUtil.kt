package newjeans.bunnies.newjeansbunnies.global.utils

import jakarta.annotation.PostConstruct
import net.nurigo.sdk.NurigoApp.initialize
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.model.MessageType
import net.nurigo.sdk.message.request.SingleMessageSendingRequest
import net.nurigo.sdk.message.response.SingleMessageSentResponse
import net.nurigo.sdk.message.service.DefaultMessageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SmsUtil(
    @Value("\${coolsms.api.key}")
    private val apiKey: String,
    @Value("\${coolsms.api.secret}")
    private val apiSecret: String,
    @Value("\${coolsms.domain}")
    private val url: String,
    @Value("\${coolsms.phoneNumber}")
    private val phoneNumber: String,
) {
    private lateinit var messageService: DefaultMessageService

    @PostConstruct
    private fun init() {
        this.messageService = initialize(apiKey, apiSecret, url)
    }

    fun sendOne(to: String, message: String): SingleMessageSentResponse? {
        val smsMessage = Message(
            type = MessageType.SMS,
            from = phoneNumber,
            to = to,
            text = message
        )

        return messageService.sendOne(SingleMessageSendingRequest(smsMessage))
    }

}