package com.github.hanzm_10.murico.swingapp.lib.database.dao;

import java.io.IOException;
import java.sql.SQLException;

import org.jetbrains.annotations.Range;

import com.github.hanzm_10.murico.swingapp.lib.database.entity.role.Role;

public interface RoleDao {
	public Role[] getRolesOfUserById(@Range(from = 0, to = Integer.MAX_VALUE) final int _userId)
			throws IOException, SQLException;
}
