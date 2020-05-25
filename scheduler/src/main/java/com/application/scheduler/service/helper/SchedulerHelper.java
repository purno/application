package com.application.scheduler.service.helper;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SchedulerHelper {
    List<Object> fetchObjectList(Pageable pageRequest);
}
