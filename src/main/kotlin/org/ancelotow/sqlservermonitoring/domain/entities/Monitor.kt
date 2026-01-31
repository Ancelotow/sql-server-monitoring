package org.ancelotow.sqlservermonitoring.domain.entities

data class Monitor(
    val processorTime: Int,
    val waitingTask: Int,
    val databaseIO: Int,
    val batchRequests: Int,
    val ramUsage: Int,
    val tempDbDiskUsage: Int
) {
    companion object {
        val empty = Monitor(0, 0, 0, 0, 0, 0)
    }
}
