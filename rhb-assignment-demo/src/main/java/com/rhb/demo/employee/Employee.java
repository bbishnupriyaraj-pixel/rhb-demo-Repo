package com.rhb.demo.employee;

import com.rhb.demo.department.Department;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, length = 100)
	private String firstName;
	@Column(nullable = false, length = 100)
	private String lastName;
	@Column(nullable = false, unique = true, length = 254)
	private String email;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "department_id", nullable = false)
	private Department department;

	protected Employee() {
	}

	public Employee(String firstName, String lastName, String email, Department department) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.department = department;
	}
	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	
}
