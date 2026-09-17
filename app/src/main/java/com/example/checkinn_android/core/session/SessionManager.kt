package com.example.checkinn_android.core.session

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Application-wide session event bus.
 *
 * The [TokenAuthenticator] emits [SessionEvent.Unauthorized] when a token
 * refresh fails, signalling all observers (e.g. MainActivity) to route the
 * user back to the login screen.
 */
@Singleton
class SessionManager @Inject constructor() {

    private val _events = MutableSharedFlow<SessionEvent>(extraBufferCapacity = 1)

    /** Observe this flow in UI layer to react to session changes. */
    val events: SharedFlow<SessionEvent> = _events.asSharedFlow()

    /**
     * Emit [SessionEvent.Unauthorized]. Safe to call from any thread — the
     * [MutableSharedFlow] with [extraBufferCapacity] = 1 ensures the emit
     * never suspends even when called from a blocking OkHttp thread via
     * [kotlinx.coroutines.runBlocking].
     */
    fun emitUnauthorized() {
        _events.tryEmit(SessionEvent.Unauthorized)
    }
}

sealed class SessionEvent {
    /** Fired when token refresh fails — navigate user to the login screen. */
    data object Unauthorized : SessionEvent()
}
