package com.autoservice.mobile.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<VS : ViewState>(initial: VS) : ViewModel() {

    private val _state = MutableStateFlow(initial)
    val state: StateFlow<VS> = _state.asStateFlow()

    /**
     * Безопасно обновляет текущее состояние, используя атомарную операцию.
     * @param reducer Функция, которая принимает текущее состояние и возвращает новое.
     */
    protected fun update(reducer: (VS) -> VS) {
        _state.update(reducer)
    }

    /**
     * Выполняет блок кода, автоматически оборачивая его в состояние загрузки.
     */
    @Suppress("UNCHECKED_CAST")
    protected fun request(block: suspend () -> Unit) = viewModelScope.launch {
        _state.update { it.copyWithLoading(true) as VS }
        try {
            block()
        } finally {
            _state.update { it.copyWithLoading(false) as VS }
        }
    }
}
