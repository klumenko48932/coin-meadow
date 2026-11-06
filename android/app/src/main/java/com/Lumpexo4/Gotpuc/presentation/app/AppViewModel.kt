package com.Lumpexo4.Gotpuc.presentation.app

import androidx.lifecycle.ViewModel
import com.Lumpexo4.Gotpuc.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class NavState(
    val screen: Screen = Screen.Splash,
    val depth: Int = 0
)

class AppViewModel : ViewModel() {

    private val stack = ArrayDeque<Screen>()

    private val _state = MutableStateFlow(NavState())
    val state: StateFlow<NavState> = _state.asStateFlow()

    fun navigate(target: Screen) {
        stack.addLast(_state.value.screen)
        _state.update { it.copy(screen = target, depth = stack.size) }
    }

    fun replaceTop(target: Screen) {
        _state.update { it.copy(screen = target, depth = stack.size) }
    }

    fun replaceRoot(target: Screen) {
        stack.clear()
        _state.update { it.copy(screen = target, depth = 0) }
    }

    fun back() {
        val previous = stack.removeLastOrNull() ?: return
        _state.update { it.copy(screen = previous, depth = stack.size) }
    }
}
