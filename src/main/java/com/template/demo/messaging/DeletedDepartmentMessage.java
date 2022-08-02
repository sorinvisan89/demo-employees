package com.template.demo.messaging;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class DeletedDepartmentMessage {

	private Integer departmentId;
}
