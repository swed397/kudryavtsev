package tinkoff.fintech.lab.util

import kotlin.coroutines.cancellation.CancellationException

const val API_PARAMETER_FILMS = "TOP_100_POPULAR_FILMS"

suspend fun <R> runSuspendCatching(
    action: suspend () -> R,
    onSuccess: (R) -> Unit,
    onError: () -> Unit,
) {
    runCatching { action.invoke() }
        .onSuccess(onSuccess)
        .onFailure {
            if (it is CancellationException) {
                throw it
            } else {
                onError.invoke()
            }
        }
}