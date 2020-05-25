package com.application.dao.jparepository;

import com.application.dao.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, String> {

    @Query("SELECT COUNT(emp.alias) FROM Employee emp where emp.alias = :alias ")
    int getCountOfEmployeeWithSameAlias(@Param("alias") String alias);

}
