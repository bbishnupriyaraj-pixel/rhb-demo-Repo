package com.rhb.demo.employee;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EmployeeServiceImpl employeeService;

	// --------------------------------------------------
	// DELETE EMPLOYEE
	// --------------------------------------------------

	@Test
	void deleteEmployee_success_returns200() throws Exception {
	    doNothing().when(employeeService).deleteEmployeeById(1L);

	    mockMvc.perform(delete("/employees/v1/deleteEmployee/1"))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.message")
	            .value("Employee with ID 1 deleted successfully"));

	    verify(employeeService).deleteEmployeeById(1L);
	}

	@Test
	void deleteEmployee_notFound_returns404() throws Exception {
	    doThrow(new EmployeeNotFoundException(999L))
	        .when(employeeService).deleteEmployeeById(999L);

	    mockMvc.perform(delete("/employees/v1/deleteEmployee/999"))
	        .andExpect(status().isNotFound());

	    verify(employeeService).deleteEmployeeById(999L);
	}

	// --------------------------------------------------
	// SEARCH EMPLOYEE
	// --------------------------------------------------

	@Test
	void searchEmployee_success_returns200() throws Exception {
	    Page<EmployeeResponse> page = new PageImpl<>(
	        List.of(),
	        PageRequest.of(0, 10),
	        0
	    );

	    when(employeeService.searchEmployee("", 0, 10))
	        .thenReturn(page);

	    mockMvc.perform(get("/employees/v1/searchEmployee")
	            .param("queryString", "")
	            .param("pageNo", "0")
	            .param("pageSize", "10"))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.content").isArray())
	        .andExpect(jsonPath("$.content").isEmpty());

	    verify(employeeService).searchEmployee("", 0, 10);
	}

	@Test
	void searchEmployee_invalidPage_returns400() throws Exception {
	    when(employeeService.searchEmployee("john", -1, 10))
	        .thenThrow(new IllegalArgumentException(
	            "page must be >= 0 and size must be 1..100"));

	    mockMvc.perform(get("/employees/v1/searchEmployee")
	            .param("queryString", "john")
	            .param("pageNo", "-1")
	            .param("pageSize", "10"))
	        .andExpect(status().isBadRequest());
	}

	@Test
	void searchEmployee_invalidPageSize_returns400() throws Exception {
	    when(employeeService.searchEmployee("john", 0, 101))
	        .thenThrow(new IllegalArgumentException(
	            "page must be >= 0 and size must be 1..100"));

	    mockMvc.perform(get("/employees/v1/searchEmployee")
	            .param("queryString", "john")
	            .param("pageNo", "0")
	            .param("pageSize", "101"))
	        .andExpect(status().isBadRequest());
	}
}
