package com.rhb.demo.employee;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rhb.demo.department.Department;
import com.rhb.demo.department.DepartmentRepository;

import java.util.List;

@Service
public class EmployeeServiceImpl {
	
	
	private final EmployeeRepository employees;
	private final DepartmentRepository departments;

	public EmployeeServiceImpl(EmployeeRepository employees, DepartmentRepository departments) {
		this.employees = employees;
		this.departments = departments;
	}
	@Transactional
	public Employee saveEmployee(EmployeeRequest employeeRequest) {
		
		Department department = departments.findById(employeeRequest.departmentId())
				.orElseThrow(() -> new IllegalArgumentException("Department not found"));
		return employees.save(new Employee(employeeRequest.firstName(), employeeRequest.lastName(), employeeRequest.email(), department));
	}
	
	@Transactional
	public Employee updateEmployee(Long empId, EmployeeRequest employeeRequest) {
		
		Employee emp = getEmployeeWithDepartmentById(empId);
		
		Department department = departments.findById(employeeRequest.departmentId())
				.orElseThrow(() -> new IllegalArgumentException("Department not found"));
		
		emp.setFirstName(employeeRequest.firstName());
		emp.setLastName(employeeRequest.lastName());
		emp.setEmail(employeeRequest.email());
		emp.setDepartment(department);
		
		return employees.save(emp);
	}
	
	@Transactional
	public void deleteEmployeeById(Long empId) {
		
		if (!employees.existsById(empId)) {
			throw new EmployeeNotFoundException(empId);
		}	
		employees.deleteById(empId);
	}
	
	public Employee getEmployeeWithDepartmentById(Long id) {
		return employees.findEmployeeWithDepartmentById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
	}
	
	public List<EmployeeRepository.EmployeeDepartmentView> getEmployeeByDepartmentName(String name) {
		return employees.getEmployeeByDepartmentName(name == null ? "" : name);
	}

	@Transactional(readOnly = true)
	public Page<EmployeeResponse> searchEmployee(
	        String queryString, int pageNo, int pageSize) {

	    if (pageNo < 0 || pageSize < 1 || pageSize > 100) {
	        throw new IllegalArgumentException(
	            "page must be >= 0 and size must be 1..100"
	        );
	    }

	    String query = queryString == null ? "" : queryString.trim();

	    Pageable pageable = PageRequest.of(
	        pageNo, pageSize, Sort.by("id").ascending()
	    );

	    Page<Employee> employeesPage =
	        employees.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
	            query, query, query, pageable
	        );

	    return employeesPage.map(EmployeeResponse::employeeResponse);
	}

	
	


	

	
}
