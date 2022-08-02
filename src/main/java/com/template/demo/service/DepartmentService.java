package com.template.demo.service;

import com.template.demo.entity.DepartmentEntity;
import com.template.demo.event.DepartmentDeletedEvent;
import com.template.demo.exception.MissingEntityException;
import com.template.demo.mapper.DepartmentMapper;
import com.template.demo.model.DepartmentRequestDTO;
import com.template.demo.model.DepartmentResponseDTO;
import com.template.demo.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentService {

	private final DepartmentRepository departmentRepository;
	private final DepartmentMapper departmentMapper;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	public DepartmentService(final DepartmentRepository departmentRepository, final DepartmentMapper departmentMapper,
			final ApplicationEventPublisher applicationEventPublisher) {
		this.departmentRepository = departmentRepository;
		this.departmentMapper = departmentMapper;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	public DepartmentResponseDTO createDepartment(final DepartmentRequestDTO departmentRequestDTO) {
		final String departmentName = departmentRequestDTO.getName();
		final Optional<DepartmentEntity> existing = departmentRepository.findByName(departmentName);
		if (existing.isPresent()) {
			throw new IllegalArgumentException(
					String.format("Department with name=%s already exists!", departmentName));
		}

		final DepartmentEntity toSave = departmentMapper.mapToEntity(departmentRequestDTO);
		final DepartmentEntity saved = departmentRepository.save(toSave);
		return departmentMapper.fromEntity(saved);
	}

	public DepartmentResponseDTO deleteDepartment(final Integer departmentId) {
		final DepartmentEntity toDelete = departmentRepository.findById(departmentId).orElseThrow(
				() -> new MissingEntityException(String.format("Department with id=%s does not exist", departmentId)));

		departmentRepository.delete(toDelete);
		applicationEventPublisher.publishEvent(new DepartmentDeletedEvent(this, toDelete.getId()));
		return departmentMapper.fromEntity(toDelete);
	}

	public DepartmentResponseDTO getDepartment(final Integer departmentId) {
		final DepartmentEntity retrieved = departmentRepository.findById(departmentId).orElseThrow(
				() -> new MissingEntityException(String.format("Department with id=%s does not exist", departmentId)));
		return departmentMapper.fromEntity(retrieved);
	}

	public List<DepartmentResponseDTO> getDepartments(final Integer page, final Integer size) {
		final PageRequest pageRequest = PageRequest.of(page, size);

		final Page<DepartmentEntity> result = departmentRepository.findAll(pageRequest);
		final List<DepartmentEntity> contents = result.getContent();
		return contents.stream().map(departmentMapper::fromEntity).collect(Collectors.toList());

	}
}
