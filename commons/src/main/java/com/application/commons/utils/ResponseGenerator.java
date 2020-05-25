package com.application.commons.utils;

import com.application.commons.constants.AppConstants;
import com.application.commons.vo.ResultInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class ResponseGenerator {

    @Autowired
    Gson gson;

    public <T> ResponseEntity<String> generateSuccessResponse(HttpStatus httpStatus) {
        ResultInfo<String> resultInfo = new ResultInfo<String>();
        resultInfo.setStatus(AppConstants.ResultStatus.SUCCESS);
        return new ResponseEntity<String>(gson.toJsonTree(resultInfo).toString(), httpStatus);
    }


    public <T> ResponseEntity<Object> generateGenericResponse(ResultInfo<T> resultInfo, HttpStatus httpStatus) {
        return new ResponseEntity<Object>(resultInfo, httpStatus);
    }

    public <T> ResponseEntity<String> generateSuccessResponse(ResultInfo<T> resultInfo, HttpStatus httpStatus) {
        resultInfo.setStatus(AppConstants.ResultStatus.SUCCESS);
        return new ResponseEntity<String>(gson.toJsonTree(resultInfo).toString(), httpStatus);
    }

    public <T> ResponseEntity<String> generateSuccessResponse(T data, HttpStatus httpStatus) {
        ResultInfo<T> resultInfo = new ResultInfo<T>();
        resultInfo.setResult(data);
        resultInfo.setStatus(AppConstants.ResultStatus.SUCCESS);
        return new ResponseEntity<String>(gson.toJsonTree(resultInfo).toString(), httpStatus);
    }

    public <T> ResponseEntity<String> generateAcceptedResponse(T data, HttpStatus httpStatus) {
        ResultInfo<T> resultInfo = new ResultInfo<T>();
        resultInfo.setResult(data);
        resultInfo.setStatus(AppConstants.ResultStatus.ACCEPTED);
        return new ResponseEntity<String>(gson.toJsonTree(resultInfo).toString(), httpStatus);
    }


    public <T> ResponseEntity<String> generateFailureResponse(ResultInfo<T> resultInfo, HttpStatus httpStatus) {
        resultInfo.setStatus(AppConstants.ResultStatus.FAILED);
        return new ResponseEntity<String>(gson.toJsonTree(resultInfo).toString(), httpStatus);
    }

    public ResponseEntity<String> generateInternalServerErrorResponse(Exception e) {
        JsonObject response = new JsonObject();
        response.addProperty("Timestamp", LocalDateTime.now().toString());
        response.addProperty("Cause", e.getCause() == null ? null : e.toString());
        response.addProperty("RootCause", ExceptionUtils.getRootCause(e) == null ? null : ExceptionUtils.getRootCause(e).toString());
        response.addProperty("statusCode", 500);
        return new ResponseEntity<String>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
