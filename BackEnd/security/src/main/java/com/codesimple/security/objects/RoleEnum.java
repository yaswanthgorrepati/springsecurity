package com.codesimple.security.objects;

public enum RoleEnum {
	ADMIN(1), INTERNAL(2), EXTERNAL(3);

	int roleId;

	RoleEnum(int roleId) {
		this.roleId = roleId;
	}

	public int getRoleId() {
		return roleId;
	}
}
