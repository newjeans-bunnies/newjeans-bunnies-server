package newjeans.bunnies.newjeansbunnies.global.security.jwtimport org.springframework.boot.context.properties.ConfigurationProperties@ConfigurationProperties(prefix = "jwt")class JwtProperties(    secretKey: String,    accessExp: Int,    refreshExp: Int) {    val accessExp: Int = accessExp * 60    val refreshExp: Int = refreshExp * 60    val key = secretKey}