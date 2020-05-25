package com.application.scheduler.controller;

import com.application.commons.config.statsd.MetricUtils;
import com.application.commons.constants.AppConstants.ResultStatus;
import com.application.commons.utils.ResponseGenerator;
import com.application.commons.vo.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthCheckController {

    @Autowired
    ResponseGenerator responseGenerator;

    @Autowired
    MetricUtils metricUtils;

    @GetMapping(value = "_status")
    public ResponseEntity<String> doHealthCheck() {
        log.info("I'm Scheduler. I am perfectly alright! You can take my services :)");
        ResultInfo<String> successResponse = new ResultInfo<String>();
        successResponse.setStatus(ResultStatus.SUCCESS);
        return responseGenerator.generateSuccessResponse(successResponse, HttpStatus.OK);
    }
}
