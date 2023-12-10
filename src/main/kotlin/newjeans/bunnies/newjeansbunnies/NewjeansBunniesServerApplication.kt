package newjeans.bunnies.newjeansbunnies


import newjeans.bunnies.newjeansbunnies.global.security.jwt.JwtProperties

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication


@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
class NewjeansBunniesServerApplication

fun main(args: Array<String>) {
    runApplication<NewjeansBunniesServerApplication>(*args)
}
