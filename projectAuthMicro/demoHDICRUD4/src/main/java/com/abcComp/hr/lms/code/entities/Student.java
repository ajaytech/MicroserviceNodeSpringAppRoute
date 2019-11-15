package com.abcComp.hr.lms.code.entities;

public class Student {
	
	public int id;
	public String firstName;
	public String lastName;
	public String locationStudent;
	
	public Student(
				int id,
				String firstName,
				String lastName,
				String locationStudent){
		
		this.id= id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.locationStudent = locationStudent;
	}

}
