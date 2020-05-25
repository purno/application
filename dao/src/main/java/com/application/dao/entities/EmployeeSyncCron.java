package com.application.dao.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "EMPLOYEE_SYNC_CRON",
        indexes = {
                @Index(name = "INDEX_EMPLOYEE_ID", columnList = "EMPLOYEE_ID", unique = true)
        })
@NoArgsConstructor
public class EmployeeSyncCron extends BaseEntity implements Serializable {
    public static final long  serialVersionUID = 207095258L;

    @Id
    @Column(name = "EMPLOYEE_ID",columnDefinition = "VARCHAR(45)", nullable = false)
    String employeeId;

    @Column(name = "NEXT_TRIGGER_TIME", nullable = false)
    LocalDateTime nextTriggerTime = LocalDateTime.now();

    @Column(name="INITIATED_CRON_COUNT")
    private int initiatedCronCount;

//    @ToString.Exclude
//    @OneToOne(optional = true, fetch = FetchType.LAZY)
//    @JoinColumn(name="EMPLOYEE_ID",referencedColumnName="ID")
//    private Employee employee;


}
