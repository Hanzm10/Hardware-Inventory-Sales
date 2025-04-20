package com.github.hanzm_10.murico.swingapp.lib.database.dao;

import java.sql.SQLException;
import org.jetbrains.annotations.NotNull;

public interface UserCredentialsDao {
    /**
     * Retrieves the user password based on the provided user display name.
     *
     * @param _userDisplayName The display name of the user.
     * @return The password of the user as a String.
     * @throws SQLException If there is an error while accessing the database.
     */
    String getUserPasswordByUserDisplayName(@NotNull final String _userDisplayName)
            throws SQLException;
}
