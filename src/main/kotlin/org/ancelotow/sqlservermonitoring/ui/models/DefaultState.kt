package org.ancelotow.sqlservermonitoring.ui.models

abstract class DefaultState<T>(
    val status: Status = DefaultStateStatus.INITIAL,
    val errors: List<ErrorType> = emptyList()
) where T : DefaultState<T>, T : ViewModelState {

    abstract fun init(): T

    abstract fun loading(): T

    abstract fun error(errors: List<ErrorType>): T

}
