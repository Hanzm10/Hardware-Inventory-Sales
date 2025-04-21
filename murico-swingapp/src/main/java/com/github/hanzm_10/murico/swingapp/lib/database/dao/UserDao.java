package com.github.hanzm_10.murico.swingapp.lib.database.dao;

import java.sql.SQLException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import com.github.hanzm_10.murico.swingapp.lib.database.entity.user.User;

public interface UserDao {
    public User getUserByDisplayName(@NotNull String _userDisplayName) throws SQLException;

    public User getUserByEmail(@NotNull String _userEmail) throws SQLException;

    public User getUserById(@Range(from = 0, to = Integer.MAX_VALUE) int _userID)
            throws SQLException;
}
