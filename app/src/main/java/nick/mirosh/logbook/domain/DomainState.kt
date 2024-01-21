package nick.mirosh.logbook.domain

/**
 * Personally I don't like this domain states, they send to make it to complex and you have to
 * write more uni tests.
 *
 * I would use a Response / Result object with only Success & failure.
 */
sealed class DomainState<out T> {
    data class Success<out T>(val data: T) : DomainState<T>()
    data object Loading : DomainState<Nothing>()
    data object Empty : DomainState<Nothing>()
    data class Error(val message: String?) : DomainState<Nothing>()
}