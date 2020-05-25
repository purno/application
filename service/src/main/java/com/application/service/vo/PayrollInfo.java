package com.application.service.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class PayrollInfo implements Serializable {
    public static final long                    serialVersionUID = 20749595258L;

    String salary;

}
