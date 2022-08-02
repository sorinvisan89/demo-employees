package com.template.demo.controller;

import com.template.demo.api.EmployeesApiDelegate;
import com.template.demo.model.EmployeeRequestDTO;
import com.template.demo.model.EmployeeResponseDTO;
import com.template.demo.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class EmployeeController implements EmployeesApiDelegate {

	private final EmployeeService employeeService;

	@Autowired
	public EmployeeController(final EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@Override
	public ResponseEntity<EmployeeResponseDTO> addEmployee(final EmployeeRequestDTO employeeRequestDTO) {
		final EmployeeResponseDTO result = this.employeeService.addEmployee(employeeRequestDTO);
		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<EmployeeResponseDTO> deleteEmployee(final Integer employeeId) {
		final EmployeeResponseDTO result = this.employeeService.deleteEmployee(employeeId);
		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<EmployeeResponseDTO> getEmployee(final Integer employeeId) {
		final EmployeeResponseDTO result = this.employeeService.getEmployee(employeeId);
		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<List<EmployeeResponseDTO>> getEmployees(final Integer page, final Integer size) {
		final List<EmployeeResponseDTO> result = this.employeeService.getEmployees(page, size);
		return ResponseEntity.ok(result);
	}
}
