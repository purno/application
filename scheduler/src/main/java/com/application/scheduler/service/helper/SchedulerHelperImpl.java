package com.application.scheduler.service.helper;

import com.application.dao.annotations.ReadOnly;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SchedulerHelperImpl implements SchedulerHelper{


    @Override
    @ReadOnly
    public List<Object> fetchObjectList(Pageable pageRequest) {
        return new ArrayList<Object>();
    }

}
