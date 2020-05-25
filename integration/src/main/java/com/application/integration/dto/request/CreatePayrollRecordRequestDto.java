package com.application.integration.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePayrollRecordRequestDto {
    String name;
    String salary;
    String age;

    public void validate(){
        Preconditions.checkArgument(Objects.nonNull(name) , " Name is pre requisite ");
        Preconditions.checkArgument(Objects.nonNull(salary), "salary is required");
        try {
            BigDecimal salaryInBigDecimal = BigDecimal.valueOf(Long.parseLong(salary));
            Preconditions.checkArgument(salaryInBigDecimal.compareTo(BigDecimal.ZERO)>=0, " Salary is not valid");
        } catch (Exception ex){
            Preconditions.checkArgument(false, "salary is not of type big decimal");
        }
        Preconditions.checkArgument(Objects.nonNull(age), " age is required");
        try {
            Integer ageInNumber = Integer.parseInt(age);
            Preconditions.checkArgument(ageInNumber >= 1 , " Minimum age is of 1 year ");
        } catch (Exception ex){
            Preconditions.checkArgument(false, " age is not of forat integer");
        }
    }
}
