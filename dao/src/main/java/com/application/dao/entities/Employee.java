package com.application.dao.entities;

import com.application.commons.utils.CommonUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString
@Table(name = "EMPLOYEE",
        indexes = {
                @Index(name = "INDEX_ALIAS", columnList = "ALIAS"),
                @Index(name = "INDEX_NAME", columnList = "NAME")
        })
public class Employee extends BaseEntity implements Serializable {

    public static final long                    serialVersionUID = 2070349595258L;

    @Id
    @Column(name = "ID",columnDefinition = "VARCHAR(45)")
    String Id = CommonUtil.generateUniqueId("EMP");

    @Version
    @Column(name = "VERSION", columnDefinition = "BIGINT(20)")
    private long version;

    @Column(name = "NAME", nullable = false, columnDefinition = "VARCHAR(45)")
    String name;

    @Column(name = "ALIAS", nullable = false, columnDefinition = "VARCHAR(45)")
    String alias;

    @Column(name = "AGE", nullable = false, columnDefinition = "INT(3)")
    int age;

}
