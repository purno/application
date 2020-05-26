package com.application.dao.jparepository;

import com.application.dao.entities.Employee;
import com.application.dao.enums.SyncStatus;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, String> {


    @Query("SELECT COUNT(emp.alias) FROM Employee emp where emp.alias = :alias ")
    int getCountOfEmployeeWithSameAlias(@Param("alias") String alias);


    @Query(" SELECT emp from Employee emp where (:name is null or emp.name = :name) " +
            " and ( COALESCE(:status) is null or emp.syncStatus in (:status) ) " +
            " and ( :age < 1 or emp.age = :age ) " +
            " and (:alias is null or emp.alias = :alias )"+
            " and ( COALESCE(:empId) is null or emp.Id in (:empId) )")
    List<Employee> getApplicableEmployeeList(@Param("name") String name,
                                             @Param("status") List<SyncStatus> statusList,
                                             @Param("age") int age,
                                             @Param("empId") List<String> empId,
                                             @Param("alias") String alias,
                                             Pageable pageable);
}
