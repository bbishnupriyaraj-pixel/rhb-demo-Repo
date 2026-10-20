package com.rhb.demo;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

import com.rhb.demo.department.*;
import com.rhb.demo.employee.*;

@SpringBootApplication
@EnableSpringDataWebSupport(
	    pageSerializationMode = PageSerializationMode.VIA_DTO
	)
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner seed(DepartmentRepository departments, EmployeeRepository employees) {
		return args -> {
			if (departments.count() == 0) {
				Department eng = departments.save(new Department("Engineering"));
				Department ops = departments.save(new Department("Operations"));
				Department hr = departments.save(new Department("HumanResource"));
				employees.save(new Employee("Bishnu", "Behera", "Bishnu@example.com", eng));
				employees.save(new Employee("Priya", "Behera", "Priya@example.com", eng));
				employees.save(new Employee("Raj", "kumar", "raj@example.com", ops));
				employees.save(new Employee("Bishnu1", "Behera1", "Bishnu1@example.com", ops));
				employees.save(new Employee("Priya1", "Behera1", "Priya1@example.com", hr));
				employees.save(new Employee("Raj1", "Kumar1", "raj1@example.com", hr));
			}
		};
	}
}
