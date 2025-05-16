package com.github.hanzm_10.murico.swingapp.lib.database.dao;

import java.io.IOException;
import java.sql.SQLException;

import com.github.hanzm_10.murico.swingapp.lib.database.entity.packaging.ItemPackaging;

public interface PackagingDao {
	public ItemPackaging[] getAllPackagings() throws SQLException, IOException; // SQLException, IOException
}
