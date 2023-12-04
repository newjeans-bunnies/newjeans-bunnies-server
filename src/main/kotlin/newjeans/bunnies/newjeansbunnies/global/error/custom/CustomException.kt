package newjeans.bunnies.newjeansbunnies.global.error.custom

abstract class CustomException(
    val errorProperty: CustomErrorProperty
) : RuntimeException() {
    override fun fillInStackTrace() = this
}