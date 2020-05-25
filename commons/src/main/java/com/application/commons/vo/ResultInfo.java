package com.application.commons.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.application.commons.constants.AppConstants;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultInfo<T> {

    private AppConstants.ResultStatus status;
    private String statusCode;                        //error codes goes here
    private String statusMessage;                    //error message goes here
    private T result;

    public ResultInfo(T result) {
        this.result = result;
        this.status = AppConstants.ResultStatus.SUCCESS;
        this.statusCode = null;
        this.statusMessage = null;
    }

}
