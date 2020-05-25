package com.application.listener.listen;

import com.application.commons.constants.KafkaTopics;
import org.springframework.kafka.annotation.KafkaListener;

public interface PayrollListener {
    @KafkaListener(
            topics = KafkaTopics.EMPLOYEE_INIT,
            containerFactory = "defaultListenerAutoAckContainerFactory",
            autoStartup = "${enable.kafka.listener:true}"
    )
    void processEmployeeForPayroll(String employeeId) throws Exception;
}
