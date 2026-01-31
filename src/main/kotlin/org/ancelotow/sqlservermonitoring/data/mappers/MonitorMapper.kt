package org.ancelotow.sqlservermonitoring.data.mappers

import org.ancelotow.sqlservermonitoring.data.data_sources.dto.MonitorDto
import org.ancelotow.sqlservermonitoring.domain.entities.Monitor

class MonitorMapper {

    fun rehydrate(dto: MonitorDto) : Monitor {
        return Monitor(
            processorTime = dto.processorTime,
            waitingTask = dto.waitingTask,
            databaseIO = dto.databaseIO,
            batchRequests = dto.batchRequests
        )
    }

}