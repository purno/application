package com.application.integration.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePayrollInfoResponseDto extends SinglePayrollInfoDto {


    Data data;

    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Data {
        String name;
        String salary;
        String age;
        int id;
    }


    public String getEmployeeId() {
        Preconditions.checkArgument(Objects.nonNull(this.data), " No successful data received ");
        return String.valueOf(this.data.id);
    }
}
