package com.example.demo.model;

public class Employee {

	private String Name;
	private int Age;
	private int Salary;
	private String Gender;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getAge() {
		return Age;
	}
	public void setAge(int age) {
		Age = age;
	}
	public int getSalary() {
		return Salary;
	}
	public void setSalary(int salary) {
		Salary = salary;
	}
	public String getGender() {
		return Gender;
	}
	public void setGender(String gender) {
		Gender = gender;
	}
	
	public Employee()
	{
		
	}
	
	@Override
	public String toString() {
		return "Employee [Name=" + Name + ", Age=" + Age + ", Salary=" + Salary + ", Gender="
				+ Gender + "]";
	}
}