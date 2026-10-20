package com.rhb.demo.employee;

public record EmployeeRequest(String firstName, String lastName, String email, Long departmentId) {
	
	public EmployeeRequest {
		if (firstName == null || firstName.isBlank())
			throw new IllegalArgumentException("firstName is required");
		if (lastName == null || lastName.isBlank())
			throw new IllegalArgumentException("lastName is required");
		if (email == null || !email.contains("@"))
			throw new IllegalArgumentException("valid email is required");
		if (departmentId == null)
			throw new IllegalArgumentException("departmentId is required");
	}
}
