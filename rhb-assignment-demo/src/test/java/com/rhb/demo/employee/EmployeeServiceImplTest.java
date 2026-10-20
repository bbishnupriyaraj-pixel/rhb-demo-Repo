
package com.rhb.demo.employee;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import com.rhb.demo.department.Department;
import com.rhb.demo.department.DepartmentRepository;

@SpringBootTest
@ActiveProfiles("test")
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @MockBean
    private EmployeeRepository employees;

    @MockBean
    private DepartmentRepository departments;

    private EmployeeRequest request;

    @BeforeEach
    void setUp() {
        Mockito.reset(employees, departments);

        request = new EmployeeRequest(
            "John", "Smith", "john@example.com", 1L
        );
    }

    // =========================================
    // 1. SAVE EMPLOYEE
    // =========================================

    @Test
    void saveEmployee_success() {
        Department department = mock(Department.class);

        when(departments.findById(1L))
            .thenReturn(Optional.of(department));

        when(employees.save(any(Employee.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Employee result = employeeService.saveEmployee(request);

        assertNotNull(result);
        verify(departments).findById(1L);
        verify(employees).save(any(Employee.class));
    }

    @Test
    void saveEmployee_departmentNotFound() {
        when(departments.findById(1L))
            .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> employeeService.saveEmployee(request)
        );

        assertEquals("Department not found", exception.getMessage());
        verify(employees, never()).save(any(Employee.class));
    }

    // =========================================
    // 2. UPDATE EMPLOYEE
    // =========================================

    @Test
    void updateEmployee_success() {
        Employee employee = mock(Employee.class);
        Department department = mock(Department.class);

        when(employees.findEmployeeWithDepartmentById(1L))
            .thenReturn(Optional.of(employee));

        when(departments.findById(1L))
            .thenReturn(Optional.of(department));

        when(employees.save(employee)).thenReturn(employee);

        Employee result = employeeService.updateEmployee(1L, request);

        assertSame(employee, result);

        verify(employee).setFirstName("John");
        verify(employee).setLastName("Smith");
        verify(employee).setEmail("john@example.com");
        verify(employee).setDepartment(department);
        verify(employees).save(employee);
    }

    @Test
    void updateEmployee_employeeNotFound() {
        when(employees.findEmployeeWithDepartmentById(99L))
            .thenReturn(Optional.empty());

        assertThrows(
            EmployeeNotFoundException.class,
            () -> employeeService.updateEmployee(99L, request)
        );

        verify(departments, never()).findById(anyLong());
        verify(employees, never()).save(any(Employee.class));
    }

    @Test
    void updateEmployee_departmentNotFound() {
        Employee employee = mock(Employee.class);

        when(employees.findEmployeeWithDepartmentById(1L))
            .thenReturn(Optional.of(employee));

        when(departments.findById(1L))
            .thenReturn(Optional.empty());

        assertThrows(
            IllegalArgumentException.class,
            () -> employeeService.updateEmployee(1L, request)
        );

        verify(employees, never()).save(any(Employee.class));
    }

    // =========================================
    // 3. DELETE EMPLOYEE
    // =========================================

    @Test
    void deleteEmployee_success() {
        when(employees.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(
            () -> employeeService.deleteEmployeeById(1L)
        );

        verify(employees).existsById(1L);
        verify(employees).deleteById(1L);
    }

    @Test
    void deleteEmployee_employeeNotFound() {
        when(employees.existsById(99L)).thenReturn(false);

        assertThrows(
            EmployeeNotFoundException.class,
            () -> employeeService.deleteEmployeeById(99L)
        );

        verify(employees, never()).deleteById(anyLong());
    }

    // =========================================
    // 4. GET EMPLOYEE BY ID
    // =========================================

    @Test
    void getEmployeeById_success() {
        Employee employee = mock(Employee.class);

        when(employees.findEmployeeWithDepartmentById(1L))
            .thenReturn(Optional.of(employee));

        Employee result =
            employeeService.getEmployeeWithDepartmentById(1L);

        assertSame(employee, result);
        verify(employees).findEmployeeWithDepartmentById(1L);
    }

    @Test
    void getEmployeeById_notFound() {
        when(employees.findEmployeeWithDepartmentById(99L))
            .thenReturn(Optional.empty());

        assertThrows(
            EmployeeNotFoundException.class,
            () -> employeeService.getEmployeeWithDepartmentById(99L)
        );
    }

    // =========================================
    // 5. GET EMPLOYEES BY DEPARTMENT NAME
    // =========================================

    @Test
    void getEmployeeByDepartmentName_success() {
        when(employees.getEmployeeByDepartmentName("IT"))
            .thenReturn(List.of());

        List<EmployeeRepository.EmployeeDepartmentView> result =
            employeeService.getEmployeeByDepartmentName("IT");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(employees).getEmployeeByDepartmentName("IT");
    }

    @Test
    void getEmployeeByDepartmentName_nullNameUsesEmptyString() {
        when(employees.getEmployeeByDepartmentName(""))
            .thenReturn(List.of());

        List<EmployeeRepository.EmployeeDepartmentView> result =
            employeeService.getEmployeeByDepartmentName(null);

        assertNotNull(result);
        verify(employees).getEmployeeByDepartmentName("");
    }

    // =========================================
    // 6. SEARCH EMPLOYEES WITH PAGINATION
    // =========================================

    @Test
    void searchEmployee_success() {
        Page<Employee> employeePage = new PageImpl<>(
            List.of(),
            PageRequest.of(0, 10, Sort.by("id").ascending()),
            0
        );

        when(employees
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                eq("john"), eq("john"), eq("john"), any(Pageable.class)
            )).thenReturn(employeePage);

        Page<EmployeeResponse> result =
            employeeService.searchEmployee(" john ", 0, 10);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(employees)
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                eq("john"), eq("john"), eq("john"),
                argThat(pageable ->
                    pageable.getPageNumber() == 0
                    && pageable.getPageSize() == 10
                    && pageable.getSort().getOrderFor("id") != null
                    && pageable.getSort().getOrderFor("id").isAscending()
                )
            );
    }

    @Test
    void searchEmployee_negativePageNumber() {
        assertThrows(
            IllegalArgumentException.class,
            () -> employeeService.searchEmployee("john", -1, 10)
        );

        verifyNoInteractions(employees);
    }

    @Test
    void searchEmployee_zeroPageSize() {
        assertThrows(
            IllegalArgumentException.class,
            () -> employeeService.searchEmployee("john", 0, 0)
        );

        verifyNoInteractions(employees);
    }

    @Test
    void searchEmployee_pageSizeAboveMaximum() {
        assertThrows(
            IllegalArgumentException.class,
            () -> employeeService.searchEmployee("john", 0, 101)
        );

        verifyNoInteractions(employees);
    }

    @Test
    void searchEmployee_nullQueryUsesEmptyString() {
        Page<Employee> employeePage = new PageImpl<>(List.of());

        when(employees
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                eq(""), eq(""), eq(""), any(Pageable.class)
            )).thenReturn(employeePage);

        Page<EmployeeResponse> result =
            employeeService.searchEmployee(null, 0, 10);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(employees)
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                eq(""), eq(""), eq(""), any(Pageable.class)
            );
    }
}
