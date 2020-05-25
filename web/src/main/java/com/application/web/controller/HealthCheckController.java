package com.application.web.controller;

import com.application.commons.vo.ResultInfo;
import com.application.commons.constants.AppConstants.ResultStatus;
import com.application.commons.utils.ResponseGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckController.class);
    @Autowired
    ResponseGenerator responseGenerator;

    @GetMapping(value = "_status")
    public ResponseEntity<String> doHealthCheck() {
        LOGGER.info("I'm perfectly alright! You can take my services :)");
        ResultInfo<String> successResponse = new ResultInfo<String>();
        successResponse.setStatus(ResultStatus.SUCCESS);
        return responseGenerator.generateSuccessResponse(successResponse, HttpStatus.OK);
    }
}
