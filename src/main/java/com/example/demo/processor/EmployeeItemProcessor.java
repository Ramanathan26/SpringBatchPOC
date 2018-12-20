package com.example.demo.processor;

import org.springframework.batch.item.ItemProcessor;

import com.example.demo.model.Employee;

public class EmployeeItemProcessor implements ItemProcessor<Employee, Employee> {

 @Override
 public Employee process(Employee employee) throws Exception {
  
	 System.out.println("Inserting Employee : " + employee);
     return employee;
 }

} 