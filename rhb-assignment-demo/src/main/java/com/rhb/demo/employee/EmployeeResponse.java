package com.rhb.demo.employee;

public record EmployeeResponse(Long id, String firstName, String lastName, String email, Long departmentId,String departmentName) {
	
	public static EmployeeResponse employeeResponse(Employee e) {
		return new EmployeeResponse(e.getId(), e.getFirstName(), e.getLastName(), e.getEmail(),
				e.getDepartment().getId(), e.getDepartment().getName());
	}
}
