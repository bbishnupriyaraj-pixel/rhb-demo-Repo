package com.rhb.demo.department;

import jakarta.persistence.*;

@Entity
@Table(name = "departments")
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true, length = 100)
	private String name;

	protected Department() {
	}
	
	public Long getId() {
		return id;
	}

	public Department(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
