package com.template.demo.controller;

import com.template.demo.api.DepartmentsApiDelegate;
import com.template.demo.model.DepartmentRequestDTO;
import com.template.demo.model.DepartmentResponseDTO;
import com.template.demo.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class DepartmentsController implements DepartmentsApiDelegate {

	private final DepartmentService departmentService;

	@Autowired
	public DepartmentsController(final DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@Override
	public ResponseEntity<DepartmentResponseDTO> createDepartment(final DepartmentRequestDTO departmentRequestDTO) {
		final DepartmentResponseDTO result = this.departmentService.createDepartment(departmentRequestDTO);
		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<DepartmentResponseDTO> deleteDepartment(final Integer departmentId) {
		final DepartmentResponseDTO result = this.departmentService.deleteDepartment(departmentId);
		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<DepartmentResponseDTO> getDepartment(final Integer departmentId) {
		final DepartmentResponseDTO result = this.departmentService.getDepartment(departmentId);
		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<List<DepartmentResponseDTO>> getDepartments(final Integer page, final Integer size) {
		final List<DepartmentResponseDTO> result = this.departmentService.getDepartments(page, size);
		return ResponseEntity.ok(result);
	}
}
