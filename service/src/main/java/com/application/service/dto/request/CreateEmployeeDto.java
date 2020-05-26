package com.application.service.dto.request;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CreateEmployeeDto {

    String name;
    BigDecimal salary;
    int age;


    public void validate(){
        Preconditions.checkArgument(Objects.nonNull(name), " Name of the employee must be present ");
        Preconditions.checkArgument(Objects.nonNull(salary) && salary.compareTo(BigDecimal.ZERO) >=0, "Minimum salary allowed is 0 ");
        Preconditions.checkArgument(age > 0, "Minimum age allowed is 1");

    }
}
