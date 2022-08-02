package com.template.demo.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class DepartmentDeletedEvent extends ApplicationEvent {

	private Integer departmentId;

	public DepartmentDeletedEvent(final Object source, final Integer departmentId) {
		super(source);
		this.departmentId = departmentId;
	}
}
