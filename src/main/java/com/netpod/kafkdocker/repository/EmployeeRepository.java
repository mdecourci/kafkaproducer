package com.netpod.kafkdocker.repository;

import com.netpod.kafkdocker.documents.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface EmployeeRepository extends MongoRepository<Employee, String>, QueryDslPredicateExecutor<Employee> {
    Employee findByEmail(String pEmail);
}
