package com.github.hanzm_10.murico.swingapp.lib.database.dao;

import java.io.IOException;
import java.sql.SQLException;

import com.github.hanzm_10.murico.swingapp.lib.database.entity.category.ItemCategory;

public interface CategoryDao {
	public ItemCategory[] getAllCategories() throws SQLException, IOException;
}
