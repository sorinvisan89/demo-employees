package com.template.demo.mapper;

import com.template.demo.entity.DepartmentEntity;
import com.template.demo.model.DepartmentRequestDTO;
import com.template.demo.model.DepartmentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DepartmentMapper {

	@Mappings({@Mapping(target = "id", ignore = true)})
	DepartmentEntity mapToEntity(final DepartmentRequestDTO departmentRequestDTO);

	DepartmentResponseDTO fromEntity(final DepartmentEntity entity);
}
