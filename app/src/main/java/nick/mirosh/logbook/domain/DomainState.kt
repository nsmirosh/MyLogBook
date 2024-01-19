package nick.mirosh.logbook.domain

sealed class DomainState<out T> {
    data class Success<out T>(val data: T) : DomainState<T>()
    data object Loading : DomainState<Nothing>()
    data object Empty : DomainState<Nothing>()
    data class Error(val message: String?) : DomainState<Nothing>()
}