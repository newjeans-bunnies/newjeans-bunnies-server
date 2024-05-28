package newjeans.bunnies.newjeansbunnies.global

import org.springframework.security.core.annotation.AuthenticationPrincipal

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : #this.getUsername()")
annotation class AuthenticatedPrincipalId
