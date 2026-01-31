package org.ancelotow.sqlservermonitoring.domain.interactors

import org.ancelotow.sqlservermonitoring.domain.uses_cases.GetDataSourcesUseCase
import org.ancelotow.sqlservermonitoring.domain.uses_cases.GetMonitorUseCase

data class MonitoringInteractor(
    val getDataSources: GetDataSourcesUseCase,
    val getMonitor: GetMonitorUseCase
)