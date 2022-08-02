package com.template.demo.repository;

import com.template.demo.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Integer> {

	Optional<DepartmentEntity> findByName(final String name);
}
