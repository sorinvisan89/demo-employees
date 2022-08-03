package com.template.demo.domain.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserInfo {

	private Integer userId;
	private String sessionId;
	private List<String> roles;
}
