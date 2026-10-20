package com.rhb.demo.employee;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/employees/v1/")
public class EmployeeController {
	
	private final EmployeeServiceImpl employeeService;

	public EmployeeController(EmployeeServiceImpl employeeService) {
		this.employeeService = employeeService;
	}
	//# a.	Different types of http methods
	@PostMapping("/createEmployee")
	public ResponseEntity<EmployeeResponse> saveEmployee(@RequestBody EmployeeRequest r) {
		
		return ResponseEntity.status(HttpStatus.CREATED).body(EmployeeResponse.employeeResponse(employeeService.saveEmployee(r)));
	}

	@PutMapping("/updateEmployee/{id}")
	public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequest r) {
		
		return ResponseEntity.status(HttpStatus.OK).body(EmployeeResponse.employeeResponse(employeeService.updateEmployee(id, r)));
	}
	
	@DeleteMapping("/deleteEmployee/{id}")
	public ResponseEntity<Map<String, String>> deleteEmployeeById(
	        @PathVariable Long id) {

	    employeeService.deleteEmployeeById(id);

	    Map<String, String> response = Map.of(
	        "message", String.format("Employee with ID %s deleted successfully" , id));

	    return ResponseEntity.ok(response);
	}
	@GetMapping("/getEmployee/{id}")
	public EmployeeResponse getEmployeeWithDepartmentById(@PathVariable Long id) {
		return EmployeeResponse.employeeResponse(employeeService.getEmployeeWithDepartmentById(id));
	}
	

	@GetMapping("/getDepartment")
	public List<EmployeeRepository.EmployeeDepartmentView> getEmployeeByDepartmentName(@RequestParam(defaultValue = "") String name) {
		return employeeService.getEmployeeByDepartmentName(name);
	}
	
	//#b.	Implement a search method with pagination

	@GetMapping("/searchEmployee")
	public ResponseEntity<Page<EmployeeResponse>> searchEmployee(
	        @RequestParam(defaultValue = "") String queryString,
	        @RequestParam(defaultValue = "0") int pageNo,
	        @RequestParam(defaultValue = "10") int pageSize) {

	    Page<EmployeeResponse> result =
	        employeeService.searchEmployee(queryString, pageNo, pageSize);

	    return ResponseEntity.ok(result);
	}

}
