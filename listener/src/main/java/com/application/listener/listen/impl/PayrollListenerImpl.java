package com.application.listener.listen.impl;

import com.application.commons.constants.KafkaTopics;
import com.application.listener.listen.PayrollListener;
import com.application.service.PayrollService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayrollListenerImpl implements PayrollListener {


    @Autowired
    PayrollService payrollService;

    /**
     * Process and create the payroll record info > make a sync call to Payroll and crete a record
     *
     * @param employeeId
     * @throws Exception
     */
    @KafkaListener(
            topics = KafkaTopics.EMPLOYEE_INIT,
            containerFactory = "defaultListenerAutoAckContainerFactory",
            autoStartup = "${enable.kafka.listener:true}"
    )
    @Override
    public void processEmployeeForPayroll(String employeeId) throws Exception {
        try {
            log.info(String.format(" Payroll listener start-->  initiating process for employee %s ", employeeId));
            payrollService.syncPayrollWithEmployeeData(employeeId);
            log.info(String.format("  Payroll listener END -->  processed employee %s ", employeeId));
        } catch (Exception e) {
            log.error("Exception caught in initiating a sync call for record creation to Payroll.."+e.getMessage(),e);
            throw e;
        }
    }
}
