package org.ancelotow.sqlservermonitoring.ui.models

interface DefaultViewModel<S, E> where S : DefaultState<S>, S : ViewModelState, E : ViewModelEvent {
    val state: S
    fun onEvent(event: E)
}