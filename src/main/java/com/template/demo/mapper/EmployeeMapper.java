package com.template.demo.mapper;

import com.template.demo.domain.entity.EmployeeEntity;
import com.template.demo.model.EmployeeRequestDTO;
import com.template.demo.model.EmployeeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EmployeeMapper {

	@Mappings({@Mapping(target = "id", ignore = true)})
	EmployeeEntity mapToEntity(final EmployeeRequestDTO employeeRequestDTO);

	EmployeeResponseDTO fromEntity(final EmployeeEntity entity);
}
