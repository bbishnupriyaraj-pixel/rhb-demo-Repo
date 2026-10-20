package com.rhb.demo.employee;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	//#b. method with pagination
	Page<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
			String firstName, String lastName, String email, Pageable pageable);

	@Query("select e from Employee e join fetch e.department where e.id=:id")
	Optional<Employee> findEmployeeWithDepartmentById(@Param("id") Long id);

	//# e.	Sample query that joins 2 tables
	
	@Query("select e.id as id,e.firstName as firstName,e.lastName as lastName,e.email as email,d.name as departmentName from Employee e join e.department d where lower(d.name) like lower(concat('%',:department,'%'))")
	List<EmployeeDepartmentView> getEmployeeByDepartmentName(@Param("department") String department);

	interface EmployeeDepartmentView {
		Long getId();

		String getFirstName();

		String getLastName();

		String getEmail();

		String getDepartmentName();
	}
}
