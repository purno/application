package com.application.dao.entities;

import com.application.commons.utils.CommonUtil;
import com.application.commons.utils.JsonUtils;
import com.application.dao.constants.EmployeeMetaDataKeys;
import com.application.dao.enums.SyncStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@ToString
@Table(name = "EMPLOYEE",
        indexes = {
                @Index(name = "INDEX_ALIAS", columnList = "ALIAS"),
                @Index(name = "INDEX_NAME", columnList = "NAME", unique = true),
                @Index(name = "INDEX_PAYROLL_ID", columnList = "PAY_ROLL_EMP_ID", unique = true)
        })
@NoArgsConstructor
public class Employee extends BaseEntity implements Serializable {

    public static final long                    serialVersionUID = 2070349595258L;

    @Id
    @Column(name = "ID",columnDefinition = "VARCHAR(45)")
    String Id = CommonUtil.generateUniqueId("EMP");

    @Column(name = "PAY_ROLL_EMP_ID",columnDefinition = "VARCHAR(20)")
    String payRollEmpId;

    @Version
    @Column(name = "VERSION", columnDefinition = "BIGINT(20)")
    private long version;

    @Column(name = "NAME", nullable = false, columnDefinition = "VARCHAR(45)")
    String name;

    @Column(name = "ALIAS", nullable = false, columnDefinition = "VARCHAR(45)")
    String alias;

    @Column(name = "AGE", nullable = false, columnDefinition = "INT(3)")
    int age;

    @Column(name = "SYNC_STATUS", nullable = false, columnDefinition = "VARCHAR(45)")
    SyncStatus syncStatus = SyncStatus.PENDING;

    @Column(name = "META_INFO", nullable = true, columnDefinition = "text")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String metaInfo;


//    @OneToOne(mappedBy = "employee",cascade = CascadeType.ALL, optional = true, fetch = FetchType.LAZY)
//    private EmployeeSyncCron cron;



    /**
     * return the metaInfo map
     * @return
     */
    public Map<String, String> getMetaInfo() {
        if (StringUtils.isBlank(metaInfo))
            return new HashMap<String, String>();

        try {
            return JsonUtils.deserializeToMap(metaInfo, String.class, String.class);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't construct a map from the metainfo.. " + metaInfo);
        }
    }

    /**
     * the additional data consist of below parameters
     * {@link EmployeeMetaDataKeys}
     *
     * @param additionalData
     * @throws JsonProcessingException
     */
    public void setMetaInfo(Map<String, String> additionalData) throws JsonProcessingException {
        this.metaInfo = JsonUtils.serialize(additionalData);
    }



    public boolean isSyncPending(){
        return SyncStatus.getListOfStatusForSyncPending().contains(this.syncStatus);
    }


}
