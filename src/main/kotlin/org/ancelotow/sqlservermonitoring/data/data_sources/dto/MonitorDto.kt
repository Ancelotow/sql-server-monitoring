package org.ancelotow.sqlservermonitoring.data.data_sources.dto

data class MonitorDto(
    val processorTime: Int,
    val waitingTask: Int,
    val databaseIO: Int,
    val batchRequests: Int
)
