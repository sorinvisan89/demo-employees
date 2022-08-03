package com.template.demo.service;

import com.template.demo.domain.entity.EmployeeEntity;
import com.template.demo.exception.MissingEntityException;
import com.template.demo.mapper.EmployeeMapper;
import com.template.demo.model.EmployeeRequestDTO;
import com.template.demo.model.EmployeeResponseDTO;
import com.template.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;

	@Autowired
	public EmployeeService(final EmployeeRepository employeeRepository, final EmployeeMapper employeeMapper) {
		this.employeeRepository = employeeRepository;
		this.employeeMapper = employeeMapper;;
	}

	public EmployeeResponseDTO addEmployee(final EmployeeRequestDTO employeeRequestDTO) {
		final String employeeName = employeeRequestDTO.getName();
		final Optional<EmployeeEntity> existing = employeeRepository.findByName(employeeName);
		if (existing.isPresent()) {
			throw new IllegalArgumentException(String.format("Employee with name=%s already exists!", employeeName));
		}

		final EmployeeEntity toSave = employeeMapper.mapToEntity(employeeRequestDTO);
		final EmployeeEntity saved = employeeRepository.save(toSave);
		return employeeMapper.fromEntity(saved);
	}

	public EmployeeResponseDTO deleteEmployee(final Integer employeeId) {
		final EmployeeEntity toDelete = employeeRepository.findById(employeeId).orElseThrow(
				() -> new MissingEntityException(String.format("Employee with id=%s does not exist", employeeId)));

		employeeRepository.delete(toDelete);
		return employeeMapper.fromEntity(toDelete);
	}

	public EmployeeResponseDTO getEmployee(final Integer employeeId) {
		final EmployeeEntity retrieved = employeeRepository.findById(employeeId).orElseThrow(
				() -> new MissingEntityException(String.format("Employee with id=%s does not exist", employeeId)));
		return employeeMapper.fromEntity(retrieved);
	}

	public List<EmployeeResponseDTO> getEmployees(final Integer page, final Integer size) {
		final PageRequest pageRequest = PageRequest.of(page, size);

		final Page<EmployeeEntity> result = employeeRepository.findAll(pageRequest);
		final List<EmployeeEntity> contents = result.getContent();
		return contents.stream().map(employeeMapper::fromEntity).collect(Collectors.toList());

	}
}
