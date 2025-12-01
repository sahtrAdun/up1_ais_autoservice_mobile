package com.autoservice.mobile.core.ui

interface ViewState {
    fun copyWithLoading(isLoading: Boolean): ViewState
}
