package com.github.hanzm_10.murico.swingapp.lib.database.dao;

import java.io.IOException;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.auth.MuricoCrypt.HashedStringWithSalt;

public interface AccountDao {
	public HashedStringWithSalt getHashedPasswordWithSaltByUserDisplayName(@NotNull final String displayName)
			throws IOException, SQLException;
}
