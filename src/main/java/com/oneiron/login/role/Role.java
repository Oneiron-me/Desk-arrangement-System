package com.oneiron.login.role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
	ADMIN("ADMIN"), 
	MEMBER("MEMBER");

	private String value;
}
