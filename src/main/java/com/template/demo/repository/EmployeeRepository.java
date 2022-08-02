package com.template.demo.repository;

import com.template.demo.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

	Optional<EmployeeEntity> findByName(final String name);

	@Modifying
	@Transactional
	int deleteByDepartmentId(final Integer departmentId);
}
