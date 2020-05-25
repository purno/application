package com.application.web.controller.advices;


import com.application.commons.utils.ResponseGenerator;
import com.application.commons.vo.ResultInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice(basePackages = "com.interview.signzy.web.controller")
public class GlobalControllerAdvice {

    private static final Logger LOGGER = LogManager.getLogger(GlobalControllerAdvice.class);

    @Autowired
    ResponseGenerator responseGenerator;


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        LOGGER.error("IllegalArgumentException caught in controller advice :: " + e.getMessage(), e);
        ResultInfo<String> resultInfo = new ResultInfo<String>();
        resultInfo.setStatusMessage(e.getMessage());
        return responseGenerator.generateFailureResponse(resultInfo, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        LOGGER.error("Exception caught in controller advice :: " + e.getMessage(), e);
        return responseGenerator.generateInternalServerErrorResponse(e);
    }

}
